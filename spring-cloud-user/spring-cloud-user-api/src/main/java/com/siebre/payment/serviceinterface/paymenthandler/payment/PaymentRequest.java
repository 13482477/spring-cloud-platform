package com.siebre.payment.serviceinterface.paymenthandler.payment;

import java.util.ArrayList;
import java.util.List;

import com.siebre.payment.entity.paymentorderitem.PaymentOrderItem;

public class PaymentRequest {
	
	private String paymentWayCode;
	
	private String ip;
	
	private String orderNumber;

	private String openid;

	/**
	 * 银行卡号
	 */
	private String accountNo;

	/**
	 * 银行卡持有人
	 */
	private String accountName;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	private List<PaymentOrderItem> paymentOrderItems = new ArrayList<PaymentOrderItem>();

	public String getPaymentWayCode() {
		return paymentWayCode;
	}

	public void setPaymentWayCode(String paymentWayCode) {
		this.paymentWayCode = paymentWayCode;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<PaymentOrderItem> getPaymentOrderItems() {
		return paymentOrderItems;
	}

	public void setPaymentOrderItems(List<PaymentOrderItem> paymentOrderItems) {
		this.paymentOrderItems = paymentOrderItems;
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
}
