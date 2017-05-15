package com.siebre.basic.db;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 
 * @author 李志强
 *
 */
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
	private String basePackage;
	private boolean addToConfig = true;
	private SqlSessionFactory sqlSessionFactory;
	private DynamicSqlSessionTemplate sqlSessionTemplate;
	private String sqlSessionFactoryBeanName;
	private String sqlSessionTemplateBeanName;
	private Class<? extends Annotation> annotationClass;
	private Class<?> markerInterface;
	private ApplicationContext applicationContext;
	private String beanName;
	private boolean processPropertyPlaceHolders;
	private BeanNameGenerator nameGenerator;

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public void setMarkerInterface(Class<?> superClass) {
		this.markerInterface = superClass;
	}

	@Deprecated
	public void setSqlSessionTemplate(DynamicSqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public void setSqlSessionTemplateBeanName(String sqlSessionTemplateName) {
		this.sqlSessionTemplateBeanName = sqlSessionTemplateName;
	}

	@Deprecated
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName) {
		this.sqlSessionFactoryBeanName = sqlSessionFactoryName;
	}

	public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
		this.processPropertyPlaceHolders = processPropertyPlaceHolders;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setBeanName(String name) {
		this.beanName = name;
	}

	public BeanNameGenerator getNameGenerator() {
		return this.nameGenerator;
	}

	public void setNameGenerator(BeanNameGenerator nameGenerator) {
		this.nameGenerator = nameGenerator;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.basePackage, "Property 'basePackage' is required");
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		if (this.processPropertyPlaceHolders) {
			processPropertyPlaceHolders();
		}

		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
		scanner.setAddToConfig(this.addToConfig);
		scanner.setAnnotationClass(this.annotationClass);
		scanner.setMarkerInterface(this.markerInterface);
		scanner.setSqlSessionFactory(this.sqlSessionFactory);
		scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
		scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
		scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
		scanner.setResourceLoader(this.applicationContext);
		scanner.setBeanNameGenerator(this.nameGenerator);
		scanner.registerFilters();
		scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ",; \t\n"));
	}

	private void processPropertyPlaceHolders() {
		Map<String, PropertyResourceConfigurer> prcs = this.applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

		if ((!prcs.isEmpty()) && ((this.applicationContext instanceof GenericApplicationContext))) {
			BeanDefinition mapperScannerBean = ((GenericApplicationContext) this.applicationContext).getBeanFactory().getBeanDefinition(this.beanName);

			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			factory.registerBeanDefinition(this.beanName, mapperScannerBean);

			for (PropertyResourceConfigurer prc : prcs.values()) {
				prc.postProcessBeanFactory(factory);
			}

			PropertyValues values = mapperScannerBean.getPropertyValues();

			this.basePackage = updatePropertyValue("basePackage", values);
			this.sqlSessionFactoryBeanName = updatePropertyValue("sqlSessionFactoryBeanName", values);
			this.sqlSessionTemplateBeanName = updatePropertyValue("sqlSessionTemplateBeanName", values);
		}
	}

	private String updatePropertyValue(String propertyName, PropertyValues values) {
		PropertyValue property = values.getPropertyValue(propertyName);

		if (property == null) {
			return null;
		}

		Object value = property.getValue();

		if (value == null)
			return null;
		if ((value instanceof String))
			return value.toString();
		if ((value instanceof TypedStringValue)) {
			return ((TypedStringValue) value).getValue();
		}
		return null;
	}
}