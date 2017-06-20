package com.siebre.config;

import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfig {

	@Bean
	public Sampler defaultSampler() {
		return new AlwaysSampler();
	}
}
