package com.siebre.product.config;

import com.siebre.orm.hibernate.AuditInterceptor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Import(DatasourceConfig.class)
public class HibernateConfig {
	@Value("${hibernate.dialect}") String hibernateDialect;
	
	@Value("${hibernate.show_sql}") boolean hibernateShowSql;
	
	@Value("${hibernate.hbm2ddl.auto}") String hibernateHbm2ddlAuto;
	
	@Autowired
	DataSource dataSource;
	
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
    {
        HibernateTransactionManager htm = new HibernateTransactionManager();
        htm.setDataSource(dataSource);
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
        properties.put("hibernate.dialect", "org.hibernate.dialect.SiebreMySQL5Dialect");
        properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        properties.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
//        properties.put("hibernate.connection.provider_class", "org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl");
        properties.put("hibernate.connection.url", "jdbc:mysql://192.168.18.158:3306/siebre-message-demo?characterEncoding=utf8");
        properties.put("hibernate.connection.username", "root");
        properties.put("hibernate.connection.password", "1qaz@WSX");

        return properties;
    }


}
