package com.siebre.admin.security.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.siebre.admin.rpc.uaa.User;
import com.siebre.admin.rpc.uaa.UserRemoteService;
import com.siebre.basic.web.WebResult;

@Component
public class CustomerUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRemoteService userRemoteService;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		WebResult<User> result = this.userRemoteService.loadUserByUserName(username);
		return result.getData();
	}

}
