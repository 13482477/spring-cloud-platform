package com.siebre.payment.paymenthandler.basic.payment;

public interface PaymentInterfaceComponent<I, O> {
	
	public O handle(I request);


}
