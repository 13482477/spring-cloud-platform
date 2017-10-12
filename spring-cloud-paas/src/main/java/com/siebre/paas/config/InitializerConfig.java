package com.siebre.paas.config;//package com.siebre.config;
//
//import com.netflix.discovery.converters.Auto;
//import com.siebre.basic.db.DynamicDataSource;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceInitializedEvent;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.config.ResourceNotFoundException;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.jdbc.config.SortedResourcesFactoryBean;
//import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by jhonelee on 2017/7/27.
// */
//@Configuration
//@ConditionalOnProperty(havingValue = "spring.initializer")
//@ConditionalOnClass(value = DataSource.class)
//public class InitializerConfig {
//
//	@Configurable
//	@EnableConfigurationProperties(InitializerProperties.class)
//	public static class DbInitializer implements ApplicationListener<DataSourceInitializedEvent> {
//
//		private static final Log logger = LogFactory.getLog(DbInitializer.class);
//
//		@Autowired
//		private InitializerProperties properties;
//
//		@Autowired
//		private ApplicationContext applicationContext;
//
//		@Autowired
//		private DataSource dataSource;
//
//		private boolean initialized = false;
//
//		@PostConstruct
//		public void init() {
//			if (!this.properties.getInitialize()) {
//				logger.debug("Initialization disabled (not running DDL scripts)");
//				return;
//			}
//			if (this.applicationContext.getBeanNamesForType(DataSource.class, false, false).length > 0) {
//				this.dataSource = this.applicationContext.getBean(DynamicDataSource.class);
//			}
//			if (this.dataSource == null) {
//				logger.debug("No DataSource found so not initializing");
//				return;
//			}
//			runSchemaScripts();
//		}
//
//		private void runSchemaScripts() {
//			List<Resource> scripts = getScripts(this.properties.getSchema());
//			if (!scripts.isEmpty()) {
//				runScripts(scripts, username, password);
//				try {
//					this.applicationContext.publishEvent(new DataSourceInitializedEvent(this.dataSource));
//					// The listener might not be registered yet, so don't rely
//					// on
//					// it.
//					if (!this.initialized) {
//						runDataScripts();
//						this.initialized = true;
//					}
//				} catch (IllegalStateException ex) {
//					logger.warn("Could not send event to complete DataSource initialization (" + ex.getMessage() + ")");
//				}
//			}
//		}
//
//		@Override
//		public void onApplicationEvent(DataSourceInitializedEvent event) {
//			if (!this.properties.isInitialize()) {
//				logger.debug("Initialization disabled (not running data scripts)");
//				return;
//			}
//			// NOTE the event can happen more than once and
//			// the event datasource is not used here
//			if (!this.initialized) {
//				runDataScripts();
//				this.initialized = true;
//			}
//		}
//
//		private void runDataScripts() {
//			List<Resource> scripts = getScripts("spring.datasource.data", this.properties.getData(), "data");
//			String username = this.properties.getDataUsername();
//			String password = this.properties.getDataPassword();
//			runScripts(scripts, username, password);
//		}
//
//		private List<Resource> getScripts(String[] fileNames) {
//			List<Resource> result = new ArrayList<Resource>();
//			for (String fileName : fileNames) {
//				try {
//					String location = "classpath:sql/" + fileName;
//					SortedResourcesFactoryBean factory = new SortedResourcesFactoryBean(this.applicationContext, Collections.singletonList(location));
//					factory.afterPropertiesSet();
//					Resource[] resources = factory.getObject();
//					for (Resource resource : resources) {
//						if (resource.exists()) {
//							result.add(resource);
//						} else {
//							throw new ResourceNotFoundException(location, resource);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//			return result;
//		}
//
//		private void runScripts(List<Resource> resources, String username, String password) {
//			if (resources.isEmpty()) {
//				return;
//			}
//			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//			populator.setContinueOnError(this.properties.isContinueOnError());
//			populator.setSeparator(this.properties.getSeparator());
//			if (this.properties.getSqlScriptEncoding() != null) {
//				populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
//			}
//			for (Resource resource : resources) {
//				populator.addScript(resource);
//			}
//			DataSource dataSource = this.dataSource;
//			if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
//				dataSource = DataSourceBuilder.create(this.properties.getClassLoader()).driverClassName(this.properties.determineDriverClassName()).url(this.properties.determineUrl())
//						.username(username).password(password).build();
//			}
//			DatabasePopulatorUtils.execute(populator, dataSource);
//		}
//	}
//
//	@ConfigurationProperties(prefix = "spring.initializer")
//	public static class InitializerProperties {
//
//		private Boolean initialize = Boolean.FALSE;
//
//		private String[] schema;
//
//		public Boolean getInitialize() {
//			return initialize;
//		}
//
//		public void setInitialize(Boolean initialize) {
//			this.initialize = initialize;
//		}
//
//		public String[] getSchema() {
//			return schema;
//		}
//
//		public void setSchema(String[] schema) {
//			this.schema = schema;
//		}
//	}
//
//}
