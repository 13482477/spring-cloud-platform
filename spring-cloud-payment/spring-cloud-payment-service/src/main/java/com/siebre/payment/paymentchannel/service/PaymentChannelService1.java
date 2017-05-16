package com.siebre.payment.paymentchannel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.payment.entity.paymentchannel.PaymentChannel;

@Service
@RestController(value = "paymentService1")
public class PaymentChannelService1 {
	
	@Autowired
	private PaymentChannelService paymentChannelService;
	
	@RequestMapping(path = {"/v1/selectAll"}, method = {RequestMethod.GET})
	public List<PaymentChannel> selectAll() {
		return this.paymentChannelService.selectAll();
	}

	
}
