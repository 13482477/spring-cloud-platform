package com.siebre.payment.paymenthandler.payment;

import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;

import java.util.ArrayList;
import java.util.List;

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

	private PaymentOrder paymentOrder;

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

	public PaymentOrder getPaymentOrder() {
		return paymentOrder;
	}

	public void setPaymentOrder(PaymentOrder paymentOrder) {
		this.paymentOrder = paymentOrder;
	}
}
