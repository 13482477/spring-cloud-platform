package com.siebre.payment.paymentgateway.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 统一支付接口请求模型 v2版本，v2版本接口开发完成之后，清除v1版本的请求模型，并重构请求模型名称
 */
public class UnifiedPayRequest implements Serializable {

	@ApiModelProperty(value = "由前端维护，唯一标识一次支付请求", required = true)
	private String messageId;

	@ApiModelProperty(value = "支付回调地址", required = false)
	private String notificationUrl;

	@ApiModelProperty(value = "支付前端回调地址", required = false)
	private String returnUrl;

	@ApiModelProperty(value = "支付订单信息", required = true)
	private UnifiedPayOrder paymentOrder;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getNotificationUrl() {
		return notificationUrl;
	}

	public void setNotificationUrl(String notificationUrl) {
		this.notificationUrl = notificationUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public UnifiedPayOrder getPaymentOrder() {
		return paymentOrder;
	}

	public void setPaymentOrder(UnifiedPayOrder paymentOrder) {
		this.paymentOrder = paymentOrder;
	}
}
