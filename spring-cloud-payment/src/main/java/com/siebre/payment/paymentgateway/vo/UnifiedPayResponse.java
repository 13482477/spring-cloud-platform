package com.siebre.payment.paymentgateway.vo;

import java.io.Serializable;

public class UnifiedPayResponse implements Serializable{
	
	private static final long serialVersionUID = 4231942882144054731L;

	private String messageId;
	
	private String redirectUrl;

	private String returnUrl;

	private String returnCode;

	private String returnMessage;

	private String subsequentAction;

	private WechatJsApiParams weChatJsApiParams;

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

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
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

	public WechatJsApiParams getWeChatJsApiParams() {
		return weChatJsApiParams;
	}

	public void setWeChatJsApiParams(WechatJsApiParams weChatJsApiParams) {
		this.weChatJsApiParams = weChatJsApiParams;
	}

	public UnifiedPayResOrder getPaymentOrder() {
		return paymentOrder;
	}

	public void setPaymentOrder(UnifiedPayResOrder paymentOrder) {
		this.paymentOrder = paymentOrder;
	}
}
