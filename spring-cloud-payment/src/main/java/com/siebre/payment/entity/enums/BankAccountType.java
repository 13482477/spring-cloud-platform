package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * @author Huang Tianci
 * 银行卡类型
 */
public enum BankAccountType implements BaseEnum {

    SAVING(1, "储蓄卡"),
    ;

    public static BankAccountType valueOf(int value){
        switch (value) {
            case 1:
                return SAVING;
            default:
                return null;
        }
    }

    private BankAccountType(int value, String description) {
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
