package com.siebre.payment.policyrole.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siebre.payment.policyrole.mapper.PolicyRoleMapper;

@Service
public class PolicyRoleService {
	
	@Autowired
	private PolicyRoleMapper policyRoleMapper;

}
