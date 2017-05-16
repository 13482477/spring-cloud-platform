package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum EncryptionMode implements BaseEnum {
	
	MD5(1, "MD5"),
	
	RSA(2, "RSA"),

	RSA2(3, "RSA2")
	;

	private EncryptionMode(int value, String description) {
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
