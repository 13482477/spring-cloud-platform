package com.siebre.payment.paymentgateway.vo;

import com.siebre.payment.paymentorder.entity.PaymentOrder;

import java.io.Serializable;

public class UnifiedPayResponse implements Serializable{
	
	private static final long serialVersionUID = 4231942882144054731L;

	private String messageId;
	
	private String redirectUrl;

	private String returnCode;

	private String returnMessage;

	private String subsequentAction;

	private WechatJsApiParams wechatJsApiParams;

	private UnifiedPayResOrder paymentOrder;

	@Deprecated
	private Object body;

	@Deprecated
	public Object getBody() {
		return body;
	}

	@Deprecated
	public void setBody(Object body) {
		this.body = body;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
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

	public UnifiedPayResOrder getPaymentOrder() {
		return paymentOrder;
	}

	public void setPaymentOrder(UnifiedPayResOrder paymentOrder) {
		this.paymentOrder = paymentOrder;
	}
}
