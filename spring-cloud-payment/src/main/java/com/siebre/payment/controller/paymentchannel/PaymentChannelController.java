package com.siebre.payment.controller.paymentchannel;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.payment.entity.paymentchannel.PaymentChannel;
import com.siebre.payment.service.paymentchannel.PaymentChannelService;

import io.swagger.annotations.ApiOperation;

@RestController
public class PaymentChannelController {
	
	private static Logger logger = LoggerFactory.getLogger(PaymentChannelController.class);
	
	@Autowired
	private PaymentChannelService paymentChannelService;
	
	@ApiOperation(value = "获取用户列表", notes = "第一个测试API")  
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public List<PaymentChannel> index() {
		return paymentChannelService.selectAll();
	}

}
