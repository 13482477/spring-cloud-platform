package com.siebre.payment.paymentchannel.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.payment.entity.paymentchannel.PaymentChannel;
import com.siebre.payment.paymentchannel.controller.PaymentChannelController;
import com.siebre.payment.paymentchannel.mapper.PaymentChannelMapper;

@Service
public class PaymentChannelService {

	private static Logger logger = LoggerFactory.getLogger(PaymentChannelService.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private Registration registration;
	
	@Autowired
	private PaymentChannelMapper paymentChannelMapper;

	@RequestMapping(path = {"/v1/getAll"}, method = RequestMethod.GET)
	@Transactional
	public List<PaymentChannel> selectAll() {
		return paymentChannelMapper.selectAll();
	}
	
}
