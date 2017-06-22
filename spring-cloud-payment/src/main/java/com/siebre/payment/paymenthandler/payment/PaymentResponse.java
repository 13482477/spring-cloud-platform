package com.siebre.payment.paymenthandler.payment;

import com.siebre.payment.paymentgateway.vo.WechatJsApiParams;
import com.siebre.payment.paymentorder.entity.PaymentOrder;

public class PaymentResponse {
	
	private String payUrl;

	private String returnCode;

	private String returnMessage;

	private String subsequentAction;

	private WechatJsApiParams wechatJsApiParams;

	private PaymentOrder paymentOrder;

	@Deprecated
	private Object body;

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	@Deprecated
	public Object getBody() {
		return body;
	}

	@Deprecated
	public void setBody(Object body) {
		this.body = body;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public String getSubsequentAction() {
		return subsequentAction;
	}

	public void setSubsequentAction(String subsequentAction) {
		this.subsequentAction = subsequentAction;
	}

	public WechatJsApiParams getWechatJsApiParams() {
		return wechatJsApiParams;
	}

	public void setWechatJsApiParams(WechatJsApiParams wechatJsApiParams) {
		this.wechatJsApiParams = wechatJsApiParams;
	}

	public PaymentOrder getPaymentOrder() {
		return paymentOrder;
	}

	public void setPaymentOrder(PaymentOrder paymentOrder) {
		this.paymentOrder = paymentOrder;
	}
}
