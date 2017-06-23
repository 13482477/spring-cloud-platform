package com.siebre.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.web.WebResult;
import com.siebre.security.entity.Resource;
import com.siebre.security.metadata.ResourceRemoteService;

@RestController
public class SecurityTestController {
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@RequestMapping(value = "/allResources", method = RequestMethod.GET)
	public WebResult<List<Resource>> loadAllResources() {
		return this.resourceRemoteService.allResourcesAndPermission();
	}

}
