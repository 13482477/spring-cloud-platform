package com.siebre.admin.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.siebre.admin.authority.entity.Authority;
import com.siebre.admin.model.role.Role;
import com.siebre.admin.user.entity.User;
import com.siebre.admin.user.service.UserService;

/**
 * Created by AdamTang on 2017/3/3. Project:siebre-cloud-platform Version:1.0
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.loadUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在" + username);
		}
		List<GrantedAuthority> auths = getAuthorities(user);
		return new com.siebre.admin.user.entity.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, auths);
	}

	private List<GrantedAuthority> getAuthorities(User user) {
		Set<String> authorityStrs = new HashSet<String>();
		List<Role> roles = user.getRoles();

		for (Role role : roles) {
			for (Authority authority : role.getAuthorities()) {
				authorityStrs.add(authority.getAuthorityCode());
			}
		}

		List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		for (String authorityStr : authorityStrs) {
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authorityStr);
			auth.add(simpleGrantedAuthority);
		}

		return auth;
	}
}
