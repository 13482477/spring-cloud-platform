package com.siebre.uaa.test.resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.siebre.uaa.initializer.service.InitializerService;
import com.siebre.uaa.test.basic.DbTestConfig;

public class InitializerServiceTest extends DbTestConfig {
	
	@Autowired
	private InitializerService initializerService;
	
	@Test
	@Rollback(false)
	public void initResourceFromFileTest() {
		this.initializerService.initResource();
		this.initializerService.initAdminAuth();
	}

}
