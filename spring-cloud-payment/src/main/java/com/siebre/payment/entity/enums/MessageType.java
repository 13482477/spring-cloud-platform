package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum MessageType implements BaseEnum {
	
	ALIPAY(1, "ALIPAY"),

	;
	
	private int value;
	
	private String description;
	
	MessageType(int value, String description) {
		this.value = value;
		this.description = description;
	}
	
	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
