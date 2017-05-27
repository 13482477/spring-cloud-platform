package com.siebre.payment.paymentorderitem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siebre.payment.paymentorderitem.mapper.PaymentOrderItemMapper;

@Service
public class PaymentOrderItemService {
	
	@Autowired
	private PaymentOrderItemMapper paymentOrderItemMapper;

}
