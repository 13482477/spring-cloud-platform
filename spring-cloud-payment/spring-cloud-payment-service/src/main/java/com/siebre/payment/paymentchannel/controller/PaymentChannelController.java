package com.siebre.payment.paymentchannel.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class PaymentChannelController {
	
	private static Logger logger = LoggerFactory.getLogger(PaymentChannelController.class);
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private Registration registration;
	
	@ApiOperation(value = "获取用户列表", notes = "第一个测试API")  
	@RequestMapping(value = "/index")
	public String index() {
		
		List<ServiceInstance> serviceInstances = discoveryClient.getInstances(this.registration.getServiceId());
		ServiceInstance instance = serviceInstances.get(0);
		
		logger.info("/index, host:{}, serviceId:{}", instance.getHost(), instance.getServiceId());
		return "hello world";
	}

}