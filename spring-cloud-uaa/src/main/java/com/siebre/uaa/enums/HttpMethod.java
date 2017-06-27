
package com.siebre.uaa.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum HttpMethod implements BaseEnum {
	
	GET(1, "GET"),

	HEAD(2, "HEAD"),

	POST(3, "POST"),
	
	PUT(4, "PUT"),

	PATCH(5, "PATCH"),

	DELETE(6, "DELETE"),

	OPTIONS(7, "OPTIONS"),

	TRACE(8, "TRACE"),
	;
	
	private int value;

	private String description;

	HttpMethod(int value, String description) {
		this.value = value;
		this.description = description;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
