package com.siebre.security.userdetails;

import org.springframework.stereotype.Component;

import com.siebre.basic.web.WebResult;
import com.siebre.security.entity.User;

@Component
public class UserRemoteServiceFallback implements UserRemoteService {

	public WebResult<User> loadUserByUserName(String username) {
		return WebResult.<User>builder().returnCode("500").returnMessage("请求超时").build();
	}

}
