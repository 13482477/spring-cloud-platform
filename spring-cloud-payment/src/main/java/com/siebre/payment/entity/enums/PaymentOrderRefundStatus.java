package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum PaymentOrderRefundStatus implements BaseEnum {

	NOT_REFUND(0, "未退款"),

	PART_REFUND(1, "部分退款"),

	FULL_REFUND(2, "全额退款"),

	PROCESSING_REFUND(3, "退款处理中"),
	;

	private PaymentOrderRefundStatus(int value, String description) {
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
