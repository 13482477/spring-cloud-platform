package com.siebre.gateway.test.resource;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.siebre.basic.web.WebResult;
import com.siebre.gateway.security.entity.Resource;
import com.siebre.gateway.security.metadata.ResourceRemoteService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ResourceRemoteServiceTest {
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Test
	public void loadAllResourceAndPermissionsTest() {
		WebResult<List<Resource>> webResult = this.resourceRemoteService.allResourcesAndPermission();
		webResult.getData();
	}

}
