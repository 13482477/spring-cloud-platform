package com.siebre.uaa.authority.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.web.WebResult;
import com.siebre.uaa.authority.entity.Authority;
import com.siebre.uaa.authority.service.AuthorityService;

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
