package com.siebre.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author lizhiqiang
 *
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

//	@Autowired
//	@Qualifier("propertiesConfig")
//	private Properties propertiesConfig;

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
					.apiInfo(apiInfo())
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.siebre"))
					.paths(PathSelectors.any())
					.build();
	}
	

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("siebre cloud")
				.description("用户中心接口说明文档")
				.termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
				.contact(new Contact("李志强", "www.siebre.com", "13482477@qq.com"))
				.license("Siebre.com").licenseUrl("#")
				.version("1.0-SNAPSHOT")
				.build();
	}

}
