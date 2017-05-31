package com.siebre.payment.policylibility.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siebre.payment.policylibility.mapper.PolicyLibilityMapper;

@Service
public class PolicyLibilityService {
	
	@Autowired
	private PolicyLibilityMapper policyLibilityMapper;

}
