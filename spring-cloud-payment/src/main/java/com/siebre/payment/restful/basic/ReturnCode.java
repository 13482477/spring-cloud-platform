package com.siebre.payment.restful.basic;

public enum ReturnCode {
	
	SUCCESS("200", "成功!"),
	FAIL("500", "失败"),
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
