package com.siebre;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ImportResource;

import com.siebre.security.metadata.CustomerSecurityMetadata;

@EnableFeignClients
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication(exclude = {SessionAutoConfiguration.class, DataSourceAutoConfiguration.class, RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, TransactionAutoConfiguration.class})
@ImportResource({"classpath:spring/applicationContext-*.xml"})
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args)
		.getBean(CustomerSecurityMetadata.class)
		.initSecurityMeta();
	}

}
