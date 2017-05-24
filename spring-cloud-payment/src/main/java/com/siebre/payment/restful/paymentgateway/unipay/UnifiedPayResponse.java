package com.siebre.payment.restful.paymentgateway.unipay;

import java.io.Serializable;

public class UnifiedPayResponse implements Serializable{
	
	private static final long serialVersionUID = 4231942882144054731L;
	
	private String paymentUrl;

	private Object body;

	public String getPaymentUrl() {
		return paymentUrl;
	}

	public void setPaymentUrl(String paymentUrl) {
		this.paymentUrl = paymentUrl;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}
