package com.siebre.admin.authority.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.admin.authority.entity.Authority;
import com.siebre.admin.authority.service.AuthorityService;
import com.siebre.basic.web.WebResult;

@RestController
public class AuthorityController {
	
	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping(value = "/api/v1/authorities/resource/{resourceId}", method = {RequestMethod.GET})
	public WebResult<List<Authority>> searchAuthority(@PathVariable("resourceId") Long resourceId) {
		List<Authority> data = this.authorityService.getAuthoritiesByResourceId(resourceId);
		return WebResult.<List<Authority>>builder().returnCode("200").data(data).build();
	}

}
