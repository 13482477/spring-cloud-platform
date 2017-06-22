package com.siebre.product;


import com.siebre.agreement.dao.AgreementSpecRepository;
import com.siebre.agreement.dao.AgreementSpecRepositoryImpl;
import com.siebre.agreement.filter.SmfBehaviorInternalReferenceGenerator;
import com.siebre.agreement.io.AgreementSpecReaderFactory;
import com.siebre.agreement.io.support.XmlAgreementSpecReaderFactory;
import com.siebre.product.dao.ProductComponentRepository;
import com.siebre.product.dao.ProductComponentRepositoryImpl;
import com.siebre.product.dao.support.InsuranceProductProvider;
import com.siebre.product.repository.InsuranceProductRepository;
import com.siebre.product.repository.InsuranceProductRepositoryImpl;
import com.siebre.repository.GeneralRepository;
import com.siebre.repository.entity.RepositoryInitializer;
import com.siebre.repository.rdb.hibernate.HibernateGeneralRepository;
import com.siebre.smf.groovy.GroovySmfInvokable;
import com.siebre.smf.groovy.SpringBeanDependencyResolver;
import com.siebre.smf.invokable.DependencyResolver;
import com.siebre.smf.invokable.SmfInvokable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

//@EnableDiscoveryClient
//@EnableFeignClients
@SpringBootApplication(exclude = {SessionAutoConfiguration.class, DataSourceAutoConfiguration.class, RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
//@ImportResource({"classpath:spring/applicationContext-*.xml"})
public class App {
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Autowired
	private PlatformTransactionManager txManager;


	@Bean
	DependencyResolver smfBehaviorDependencyResolver() {
		return new SpringBeanDependencyResolver();
	}

	@Bean
	GeneralRepository generalRepository() {
		return new HibernateGeneralRepository();
	}

	@Bean
	InsuranceProductRepository productRepository() {
		return new InsuranceProductRepositoryImpl(generalRepository());
	}

	@Bean
	ProductComponentRepository productComponentRepository() {
		return new ProductComponentRepositoryImpl();
	}

	@Bean
	AgreementSpecRepository agreementSpecRepository() {
		return new AgreementSpecRepositoryImpl(generalRepository());
	}

	@Bean
	RepositoryInitializer repositoryInitializer() {
		RepositoryInitializer result = new RepositoryInitializer();
		result.setRepository(generalRepository());
		result.setTxManager(txManager);
		result.setAutoInitialize(true);
		return result;
	}

	@Bean
	InsuranceProductProvider productProvider() {
		InsuranceProductProvider result = new InsuranceProductProvider();
		result.setReaderFactory(specReaderFactory());
		return result;
	}

	@Bean
	AgreementSpecReaderFactory specReaderFactory() {
		XmlAgreementSpecReaderFactory result = new XmlAgreementSpecReaderFactory()
				.addFilter(new SmfBehaviorInternalReferenceGenerator());
		result.setMappingFile("SmfCastorMapping.xml");
		return result;
	}

	public void afterPropertiesSet() throws Exception {
		//TODO move to WebApplicationInitializer
		SmfInvokable.setImplClass(GroovySmfInvokable.class);
	}

}
