package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum PaymentTransactionStatus implements BaseEnum {

	PAY_FAILED(1, "交易失败"),
	
	PAY_PROCESSING(2, "交易中"),
	
	PAY_SUCCESS(3, "交易成功"),

	REFUND_PROCESSING(5, "退款中"),

	REFUND_SUCCESS(6, "退款成功"),

	REFUND_FAILED(7, "退款失败"),

	CLOSED(4, "交易关闭"),
	;

	private PaymentTransactionStatus(int value, String description) {
		this.value = value;
		this.description = description;
	}
	
	private int value;
	
	private String description;
	
	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
