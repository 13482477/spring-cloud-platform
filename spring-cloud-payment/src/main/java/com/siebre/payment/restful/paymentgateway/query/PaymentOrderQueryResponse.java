package com.siebre.payment.restful.paymentgateway.query;

import com.siebre.payment.entity.enums.PaymentTransactionStatus;

public class PaymentOrderQueryResponse {
	
	private PaymentTransactionStatus status;

	public PaymentTransactionStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentTransactionStatus status) {
		this.status = status;
	}

}
