package com.siebre.payment.serviceinterface.paymenthandler;

public interface PaymentInterfaceComponent<I, O> {
	
	public O handle(I request);


}
