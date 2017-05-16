package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum TransferType implements BaseEnum {
	
	NATIVE(1, "NATIVE"),
	HTTP(2, "HTTP"),
	HTTPS(3, "HTTPS"),
	;
	
	TransferType(int value, String description) {
		this.value = value;
		this.description = description;
	}
	
	private int value;
	
	private String description;

	public int getValue() {
		return this.value;
	}

	public String getDescription() {
		return this.description;
	}
	
	

}
