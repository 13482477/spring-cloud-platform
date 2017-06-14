package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * @author Huang Tianci
 * 保险人类别
 */
public enum PolicyRoleType implements BaseEnum {

    APPLICANT(1,"投保人"),

    INSURED(2, "被保人"),
    ;

    public static PolicyRoleType valueOf(int value){
        switch (value) {
            case 1:
                return APPLICANT;
            case 2:
                return INSURED;
            default:
                return null;
        }
    }

    private PolicyRoleType(int value, String description) {
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
