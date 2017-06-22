package com.siebre.payment.entity.enums;

public enum ReturnCode {
	
	SUCCESS("200", "SUCCESS"),
	FAIL("500", "FAIL"),
	;
	
	ReturnCode(String value, String description) {
		this.value = value;
		this.description = description;
	}
	
	private String value;
	
	private String description;

	public String getValue() {
		return this.value;
	}

	public String getDescription() {
		return this.description;
	}
	
	

}
