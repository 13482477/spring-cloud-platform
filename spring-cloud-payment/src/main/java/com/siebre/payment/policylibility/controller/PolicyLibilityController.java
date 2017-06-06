package com.siebre.payment.policylibility.controller;

import com.siebre.payment.policylibility.service.PolicyLibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PolicyLibilityController {
	
	@Autowired
	private PolicyLibilityService policyLibilityService;
	
	

}
