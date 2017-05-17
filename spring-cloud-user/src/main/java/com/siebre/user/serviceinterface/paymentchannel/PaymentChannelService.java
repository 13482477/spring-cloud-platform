package com.siebre.user.serviceinterface.paymentchannel;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.siebre.user.controller.user.PaymentChannelVo;

@FeignClient(name = "paymentChannelService", url = "http://SPRING-CLOUD-PAYMENT")
public interface PaymentChannelService {
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public List<PaymentChannelVo> index();

}
