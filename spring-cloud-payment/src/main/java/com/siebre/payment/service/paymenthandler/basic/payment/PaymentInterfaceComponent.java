package com.siebre.payment.service.paymenthandler.basic.payment;

public interface PaymentInterfaceComponent<I, O> {
	
	public O handle(I request);


}
