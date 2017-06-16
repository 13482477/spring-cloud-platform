package com.siebre.admin.rpc.uaa;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.siebre.basic.web.WebResult;

@FeignClient(value = "SPRING-CLOUD-UAA", fallback = UserRemoteServiceFallback.class)
public interface UserRemoteService {
	
	@RequestMapping(value = "/api/v1/user/loadUserByUserName/{username}", method = RequestMethod.GET)
	public WebResult<User> loadUserByUserName(@PathVariable("username") String username);
	
}
