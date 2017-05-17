package com.siebre.payment.service.paymentchannel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.siebre.payment.entity.paymentchannel.PaymentChannel;

@Service
public class PaymentChannelService1 {
	
	@Autowired
	private PaymentChannelService paymentChannelService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public List<PaymentChannel> selectAll() {
		
		@SuppressWarnings("rawtypes")
		ResponseEntity<List> entity = restTemplate.getForEntity("http://spring-cloud-payment/v1/getAll", List.class);
		
		entity.getBody();
		
		return this.paymentChannelService.selectAll();
	}

	
}
