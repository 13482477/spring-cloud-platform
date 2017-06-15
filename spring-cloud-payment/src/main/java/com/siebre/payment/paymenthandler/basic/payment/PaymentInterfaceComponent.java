package com.siebre.payment.paymenthandler.basic.payment;

public interface PaymentInterfaceComponent<I, O> {
	
	void handle(I request, O response);

}
