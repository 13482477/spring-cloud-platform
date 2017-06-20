package com.siebre.uaa.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum ResourceType implements BaseEnum {
	
	MENU(1, "MENU"),

	RESOURCE(2, "RESOURCE"),

	SYSTEM(3, "SYSTEM"),
	
	;
	
	private int value;

	private String description;

	ResourceType(int value, String description) {
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
