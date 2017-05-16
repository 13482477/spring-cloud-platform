package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum PaymentTransactionStatus implements BaseEnum {

	//特只由于业务或者系统导致的异常，从而使交易失败
	FAILED(1, "交易失败"),
	
	PROCESSING(2, "交易中"),
	
	SUCCESS(3, "交易成功"),

	//超出了一定时间之后没有完成交易，则设置订单关闭
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
