package com.siebre.product;


import com.siebre.agreement.dao.AgreementSpecRepository;
import com.siebre.agreement.dao.AgreementSpecRepositoryImpl;
import com.siebre.agreement.filter.SmfBehaviorInternalReferenceGenerator;
import com.siebre.agreement.io.AgreementSpecReaderFactory;
import com.siebre.agreement.io.support.XmlAgreementSpecReaderFactory;
import com.siebre.agreement.service.DefaultAgreementRequestExecutor;
import com.siebre.policy.application.service.SiebreCloudApplicationService;
import com.siebre.policy.dao.InsurancePolicyRepository;
import com.siebre.policy.repository.InsurancePolicyRepositoryImpl;
import com.siebre.product.dao.ProductComponentRepository;
import com.siebre.product.dao.ProductComponentRepositoryImpl;
import com.siebre.product.dao.support.InsuranceProductProvider;
import com.siebre.product.repository.InsuranceProductRepository;
import com.siebre.product.repository.InsuranceProductRepositoryImpl;
import com.siebre.basic.sequence.RedisBasedSequenceGenerator;
import com.siebre.basic.sequence.SequenceGenerator;
import com.siebre.repository.GeneralRepository;
import com.siebre.repository.entity.SiebreCloudRepositoryInitializer;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.hibernate4.support.OpenSessionInViewInterceptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.annotation.MultipartConfig;
import java.util.Properties;

@MultipartConfig//上传文件
//@EnableDiscoveryClient
//@EnableFeignClients
@SpringBootApplication(exclude = {SessionAutoConfiguration.class, DataSourceAutoConfiguration.class, RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@ComponentScan("com.siebre.policy.application.service;com.siebre.product;com.siebre.policy.payment")
//@ImportResource({"classpath:spring/applicationContext-*.xml"})
public class App {

    public static void main(String[] args) {
        Properties systemProperties = System.getProperties();
        systemProperties.remove("javax.xml.parsers.DocumentBuilderFactory");
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PlatformTransactionManager txManager;

    @Bean
    InsurancePolicyRepository insurancePolicyRepository() {
        return new InsurancePolicyRepositoryImpl(generalRepository());
    }

    /**
     * Spring MVC multipart/form-data上传文件支持
     *
     * @return
     */
    @Bean
    MultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        return resolver;
    }

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
    SiebreCloudRepositoryInitializer repositoryInitializer() {
        SiebreCloudRepositoryInitializer result = new SiebreCloudRepositoryInitializer();
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

    @Bean
    OpenSessionInViewInterceptor openSessionInViewInterceptor() {
        return new OpenSessionInViewInterceptor();
    }

    public void afterPropertiesSet() throws Exception {
        //TODO move to WebApplicationInitializer
        SmfInvokable.setImplClass(GroovySmfInvokable.class);
    }

    @Bean("applicationNumberGenerator")
    RedisBasedSequenceGenerator applicationNumberGenerator() {
        return new RedisBasedSequenceGenerator(redisTemplate, "application_number");
    }


    @Bean
    public SiebreCloudApplicationService applicationService(@Autowired SequenceGenerator applicationNumberGenerator) {
        return new SiebreCloudApplicationService(new DefaultAgreementRequestExecutor(), applicationNumberGenerator);
    }

}
