package com.siebre.config;

import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    Request.Options feignOptions() {
        return new Request.Options(/**connectTimeoutMillis**/60 * 1000, /** readTimeoutMillis **/60 * 1000);
    }

    @Bean
    Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

}
