package com.siebre.user.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.siebre.user.serviceinterface.paymentchannel.PaymentChannelService;

@RestController
public class UserController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PaymentChannelService paymentChannelService;
	
	@RequestMapping(path = "/getUserInfo", method = {RequestMethod.GET})
	public List<PaymentChannelVo> getUserInfo() {
		List<PaymentChannelVo> response = this.paymentChannelService.index();
		return response;
	}

}
