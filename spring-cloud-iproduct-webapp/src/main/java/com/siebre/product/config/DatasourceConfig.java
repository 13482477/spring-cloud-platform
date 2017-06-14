package com.siebre.product.config;


import java.util.Arrays;
import java.util.Properties;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import com.beust.jcommander.internal.Lists;
import com.siebre.basic.db.DynamicDataSource;
import com.siebre.basic.query.PagePlugin;

@Configuration
public class DatasourceConfig {
	
	@Bean("master")
	@Autowired
	public DataSource master(Environment env) {
		DataSource ds = new DataSource();
		ds.setDriverClassName(env.getProperty("jdbc.mysql.Driver"));
		ds.setUrl(env.getProperty("jdbc.master.url"));
		ds.setUsername(env.getProperty("jdbc.master.username"));
		ds.setPassword(env.getProperty("jdbc.master.password"));
		
		setCommonConfiguration(env, ds);
		
		return ds;
	}

	@Bean("slave")
	@Autowired
	public DataSource slave(Environment env) {
		DataSource ds = new DataSource();
		ds.setDriverClassName(env.getProperty("jdbc.mysql.Driver"));
		ds.setUrl(env.getProperty("jdbc.slave.url"));
		ds.setUsername(env.getProperty("jdbc.slave.username"));
		ds.setPassword(env.getProperty("jdbc.slave.password"));
		
		setCommonConfiguration(env, ds);
		
		return ds;
	}
	
	private void setCommonConfiguration(Environment env, DataSource ds) {
		//common configuration
		ds.setMaxActive(env.getProperty("jdbc.maxActive", Integer.class));
		ds.setInitialSize(env.getProperty("jdbc.initialSize", Integer.class));
		ds.setMinIdle(env.getProperty("jdbc.maxIdle", Integer.class));
		ds.setJdbcInterceptors(env.getProperty("jdbc.jdbcInterceptors"));
		
		//fixed setting
		ds.setTestWhileIdle(true);
		ds.setTestOnBorrow(true);
		ds.setValidationQuery("select 1");
		ds.setTestOnReturn(false);
		ds.setValidationInterval(30000);
		ds.setTimeBetweenEvictionRunsMillis(5000);
		ds.setMaxWait(15000);
		ds.setRemoveAbandoned(true);
		ds.setRemoveAbandonedTimeout(60);
		ds.setLogAbandoned(false);
		ds.setMinEvictableIdleTimeMillis(30);
		ds.setJmxEnabled(true);
		ds.setName("jdbcPool");
	}
	
	@Bean
	@Autowired 
	public DynamicDataSource dataSource(@Qualifier("master") DataSource master, @Qualifier("slave") DataSource slave) {
		DynamicDataSource dds = new DynamicDataSource();
		dds.setMaster(master);
		dds.setSlaves(Arrays.asList(slave));
		
		return dds;
	}
	
//	@Bean
//	@Autowired
//	public SqlSessionFactoryBean sqlSessionFactory(DynamicDataSource dataSource, @Value("classpath*:sqlmapper/*Mapper.xml") Resource[] rs) throws Exception {
//		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//		bean.setDataSource(dataSource);
//		
//		bean.setMapperLocations(rs);
//		
//		bean.setTypeAliasesPackage("com.siebre.**.entity");
//		bean.setTypeHandlersPackage("com.siebre.**.entity.enums");
//		
//		Properties p = new Properties();
//		p.setProperty("dialect", "mysql");
//		
//		PagePlugin pp = new PagePlugin();
//		pp.setProperties(p);
//		
//		PagePlugin[] pps = new PagePlugin[]{pp};
//		bean.setPlugins(pps);
//		
//		return bean;
//	}
	
//	@Bean
//	@Lazy
//	public MapperScannerConfigurer mapperScannerConfigurer() {
//		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
//		configurer.setAnnotationClass(org.springframework.stereotype.Repository.class);
//		configurer.setBasePackage("com.siebre.**.mapper");
//		configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//		
//		return configurer;
//	}
	
}
