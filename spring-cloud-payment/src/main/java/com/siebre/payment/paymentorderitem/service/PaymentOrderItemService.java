package com.siebre.payment.paymentorderitem.service;

import com.siebre.payment.paymentorderitem.mapper.PaymentOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderItemService {
	
	@Autowired
	private PaymentOrderItemMapper paymentOrderItemMapper;

}
