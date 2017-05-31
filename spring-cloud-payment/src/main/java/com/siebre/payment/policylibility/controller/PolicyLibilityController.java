package com.siebre.payment.policylibility.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.payment.policylibility.service.PolicyLibilityService;

@RestController
public class PolicyLibilityController {
	
	@Autowired
	private PolicyLibilityService policyLibilityService;
	
	

}
