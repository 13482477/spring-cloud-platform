package com.siebre.basic.enumutil;

public class EnumUtils {
	
	public static BaseEnum getEnumByValue(Class<? extends BaseEnum> type, int value) {
		BaseEnum[] baseEnums = type.getEnumConstants();
		for (BaseEnum baseEnum : baseEnums) {
			if (value == baseEnum.getValue()) {
				return baseEnum;
			}
		}
		return null;
	}

}
