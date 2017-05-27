package com.siebre.payment.paymentway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.converters.Auto;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;

@RestController
public class PaymentWayController {
	
	@Autowired
	private PaymentWayService paymentWayService;
	
	public ServiceResult<PaymentWay> getPaymentWayByCode(String paymentWayCode) {
		PaymentWay data = this.paymentWayMapper.getPaymentWayByCode(paymentWayCode);
		return ServiceResult.<PaymentWay>builder().success(true).data(data).build();
	}
	
	@Transactional("db")
	public ServiceResult<PaymentWay> createPaymentWayAndPaymentInterface(PaymentWay paymentWay) {
		this.paymentWayMapper.insert(paymentWay);
		
		for (PaymentInterface paymentInterface : paymentWay.getPaymentInterfaces()) {
			paymentInterface.setPaymentWayId(paymentWay.getId());
			this.paymentInterfaceMapper.insert(paymentInterface);
		}
		return ServiceResult.<PaymentWay>builder().success(true).data(paymentWay).build();
	}

}
