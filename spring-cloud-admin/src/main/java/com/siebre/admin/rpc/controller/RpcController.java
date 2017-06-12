package com.siebre.admin.rpc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.admin.rpc.payment.PaymentOrder;
import com.siebre.admin.rpc.payment.PaymentOrderRemoteService;
import com.siebre.basic.web.WebResult;

@RestController
public class RpcController {
	
	@Autowired
	private PaymentOrderRemoteService paymentOrderRemoteService;
	
	@RequestMapping(value = "/api/v1/test", method = RequestMethod.GET)
	public WebResult<List<PaymentOrder>> test() {
		return this.paymentOrderRemoteService.find(null, 100, 1, null, null, null, null);
	}

}
