package com.siebre.payment.serviceinterface.paymenthandler.payment;

public class PaymentResponse {
	
	private String payUrl;

	private Object body;

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private String payUrl;

		private Object body;
		
		public Builder payUrl(String payUrl) {
			this.payUrl = payUrl;
			return this;
		}

		public Builder body(Object body){
			this.body = body;
			return this;
		}
		
		public PaymentResponse build() {
			PaymentResponse object = new PaymentResponse();
			object.setPayUrl(this.payUrl);
			object.setBody(body);
			return object;
		}
		
	}

}
