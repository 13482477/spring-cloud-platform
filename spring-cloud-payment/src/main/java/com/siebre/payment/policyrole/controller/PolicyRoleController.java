package com.siebre.payment.policyrole.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.payment.policyrole.service.PolicyRoleService;

@RestController
public class PolicyRoleController {
	
	@Autowired
	private PolicyRoleService policyRoleService;

}
