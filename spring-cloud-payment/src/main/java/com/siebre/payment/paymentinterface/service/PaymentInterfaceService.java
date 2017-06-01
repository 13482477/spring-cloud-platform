package com.siebre.payment.paymentinterface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siebre.payment.paymentinterface.mapper.PaymentInterfaceMapper;

@Service
public class PaymentInterfaceService {
	
	@Autowired
	private PaymentInterfaceMapper paymentInterfaceMapper;
	

}
