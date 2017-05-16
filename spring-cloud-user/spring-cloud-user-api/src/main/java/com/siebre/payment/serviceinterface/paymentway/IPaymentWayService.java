package com.siebre.payment.serviceinterface.paymentway;

import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.paymentway.PaymentWay;

public interface IPaymentWayService {
	
	ServiceResult<PaymentWay> getPaymentWayByCode(String paymentWayCode);
	
	ServiceResult<PaymentWay> createPaymentWayAndPaymentInterface(PaymentWay paymentWay);

}
