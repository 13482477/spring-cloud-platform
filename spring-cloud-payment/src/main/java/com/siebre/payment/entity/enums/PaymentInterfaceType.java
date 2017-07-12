package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum PaymentInterfaceType implements BaseEnum {
	
	PAY(1, "支付"),
	
	QUERY(2, "查询"),

	REFUND(3, "退款"),

	RECONCILIATION(4, "对账"),

	PAY_NOTIFY(5,"回调"),

	REFUND_QUERY(6, "退款查询"),
	;

	private PaymentInterfaceType(int value, String description) {
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
