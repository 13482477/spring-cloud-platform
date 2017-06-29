package com.siebre.gateway.test.resource;

import com.siebre.basic.web.WebResult;
import com.siebre.security.entity.Resource;
import com.siebre.security.metadata.ResourceRemoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ResourceRemoteServiceTest {
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Test
	public void loadAllResourceAndPermissionsTest() {
		WebResult<List<Resource>> responseEntity = this.resourceRemoteService.allResourcesAndPermission();

		List<Resource> data = responseEntity.getData();


	}

}
