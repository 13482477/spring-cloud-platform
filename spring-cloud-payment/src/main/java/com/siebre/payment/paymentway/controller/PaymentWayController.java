package com.siebre.payment.paymentway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;

@RestController
public class PaymentWayController {
	
	@Autowired
	private PaymentWayService paymentWayService;
	
	@RequestMapping(value = "/api/v1/paymentWay/{paymentWayCode}", method = {RequestMethod.GET})
	public WebResult<PaymentWay> getPaymentWayByCode(@PathVariable String paymentWayCode) {
		PaymentWay data = this.paymentWayService.getPaymentWayByCode(paymentWayCode);
		return WebResult.<PaymentWay>builder().returnCode(WebResult.SUCCESS_CODE).returnMessage("创建成功").data(data).build();
	}
	
	@RequestMapping(value = "/api/v1/paymentWay", method = {RequestMethod.POST})
	public WebResult<PaymentWay> createPaymentWayAndPaymentInterface(@RequestBody PaymentWay paymentWay) {
		PaymentWay data = this.paymentWayService.createPaymentWayAndPaymentInterface(paymentWay);
		return WebResult.<PaymentWay>builder().returnCode(WebResult.SUCCESS_CODE).returnMessage("创建成功").data(data).build();
	}

}
