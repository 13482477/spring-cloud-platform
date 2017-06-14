package com.siebre.product.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import com.siebre.basic.db.DynamicDataSource;
import com.siebre.orm.hibernate.AuditInterceptor;

@Configuration
public class HibernateConfig {
	@Value("${hibernate.dialect}") String hibernateDialect;
	
	@Value("${hibernate.show_sql}") boolean hibernateShowSql;
	
	@Value("${hibernate.hbm2ddl.auto}") String hibernateHbm2ddlAuto;
	
	@Autowired
	@Qualifier("master")
	DataSource dataSource;
	
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
    {
        HibernateTransactionManager htm = new HibernateTransactionManager();
        htm.setSessionFactory(sessionFactory);
        return htm;
    }
    
    @Bean
    @Autowired
    public HibernateTemplate getHibernateTemplate(SessionFactory sessionFactory)
    {
        HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
        return hibernateTemplate;
    }
        
    @Bean
    public SessionFactory getSessionFactory()
    {
    	LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource);
		builder.scanPackages(
				"com.siebre.product", "com.siebre.agreement", 
				"com.siebre.intermediary", "com.siebre.party", 
				"com.siebre.policy", "com.siebre.smf", "com.siebre.security",
				"com.siebre.bmf.orm.hibernate")
				.setProperties(getHibernateProperties())
				.setInterceptor(new AuditInterceptor());
		return builder.buildSessionFactory();

    }

    public Properties getHibernateProperties()
    {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        
        return properties;
    }
	
}
