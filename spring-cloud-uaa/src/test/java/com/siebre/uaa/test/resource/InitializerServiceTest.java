package com.siebre.uaa.test.resource;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
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
