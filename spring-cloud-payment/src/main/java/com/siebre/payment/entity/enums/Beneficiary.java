package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * @author Huang Tianci
 * 受益人类别
 */
public enum Beneficiary implements BaseEnum {

    LEGAL_BENEFICIARY(1,"法定受益人"),

    DESIGNATED_BENEFICIARY(2,"指定受益人"),
    ;

    public static Beneficiary valueOf(int value){
        switch (value) {
            case 1:
                return LEGAL_BENEFICIARY;
            case 2:
                return DESIGNATED_BENEFICIARY;
            default:
                return null;
        }
    }

    private Beneficiary(int value, String description) {
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
