package com.siebre.payment.vo.unionpayment;

import com.siebre.payment.entity.enums.PaymentTransactionStatus;

import java.io.Serializable;

public class BasePayNotifyResponseVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3637982679411889264L;
	private String payWayCode;
	private PaymentTransactionStatus paymentStatus;

	public String getPayWayCode() {
		return payWayCode;
	}

	public void setPayWayCode(String payWayCode) {
		this.payWayCode = payWayCode;
	}

	public PaymentTransactionStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentTransactionStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


}
