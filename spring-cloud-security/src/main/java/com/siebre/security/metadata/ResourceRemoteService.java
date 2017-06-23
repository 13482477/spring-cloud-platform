package com.siebre.security.metadata;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.siebre.basic.web.WebResult;
import com.siebre.security.entity.Resource;

@FeignClient(value = "SPRING-CLOUD-UAA", fallback = ResourceRemoteServiceFallback.class)
@Order(1)
public interface ResourceRemoteService {
	
	@RequestMapping(value ="/api/v1/resourcesAndPermission", method = {RequestMethod.GET})
    public WebResult<List<Resource>> allResourcesAndPermission();
	
}
