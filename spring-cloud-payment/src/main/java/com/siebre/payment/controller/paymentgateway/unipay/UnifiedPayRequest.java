package com.siebre.payment.controller.paymentgateway.unipay;

import java.io.Serializable;
import java.util.List;

import com.siebre.payment.entity.paymentorderitem.PaymentOrderItem;

public class UnifiedPayRequest implements Serializable{

	private static final long serialVersionUID = 4231942882144054731L;
	
	private String orderNumber;
	
	private String payWayCode;

	private String ip;

	private String openid;
	
	private List<PaymentOrderItem> paymentOrderItems;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getPayWayCode() {
		return payWayCode;
	}

	public void setPayWayCode(String payWayCode) {
		this.payWayCode = payWayCode;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public List<PaymentOrderItem> getPaymentOrderItems() {
		return paymentOrderItems;
	}

	public void setPaymentOrderItems(List<PaymentOrderItem> paymentOrderItems) {
		this.paymentOrderItems = paymentOrderItems;
	}
    
}
