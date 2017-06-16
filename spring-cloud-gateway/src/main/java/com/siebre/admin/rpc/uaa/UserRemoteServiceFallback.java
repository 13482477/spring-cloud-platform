package com.siebre.admin.rpc.uaa;

import org.springframework.stereotype.Component;

import com.siebre.basic.web.WebResult;

@Component
public class UserRemoteServiceFallback implements UserRemoteService {

	public WebResult<User> loadUserByUserName(String username) {
		return WebResult.<User>builder().returnCode("500").returnMessage("请求超时").build();
	}

}
