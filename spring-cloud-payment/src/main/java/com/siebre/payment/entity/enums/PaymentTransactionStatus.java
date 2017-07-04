package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum PaymentTransactionStatus implements BaseEnum {

	PAY_FAILED(1, "交易失败"),
	
	PAY_PROCESSING(2, "交易中"),
	
	PAY_SUCCESS(3, "交易成功"),

	REFUND_PROCESSING(5, "退款中"),

	REFUND_SUCCESS(6, "退款成功"),

	REFUND_FAILED(7, "退款失败"),

	RECON_PROCESSING(8, "开始对账文件下载"),

	RECON_SUCCESS(9, "对账文件下载成功"),

	RECON_FAILED(10, "对账文件下载失败"),

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
