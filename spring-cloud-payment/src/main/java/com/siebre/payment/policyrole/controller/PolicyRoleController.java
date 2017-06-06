package com.siebre.payment.policyrole.controller;

import com.siebre.payment.policyrole.service.PolicyRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PolicyRoleController {
	
	@Autowired
	private PolicyRoleService policyRoleService;

}
