package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;


public enum PaymentChannelStatus implements BaseEnum {
	
	ENABLE(1, "启用"),
	
	DISABLE(2, "关闭"),
	;

	private PaymentChannelStatus(int value, String description) {
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
