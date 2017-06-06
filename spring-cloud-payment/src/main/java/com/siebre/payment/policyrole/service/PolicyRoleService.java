package com.siebre.payment.policyrole.service;

import com.siebre.payment.policyrole.mapper.PolicyRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolicyRoleService {
	
	@Autowired
	private PolicyRoleMapper policyRoleMapper;

}
