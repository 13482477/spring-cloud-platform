package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * Created by meilan on 2017/5/10.
 */
public enum SellingChannel implements BaseEnum {

    MOBILE_SALE_APP(1,"移动展业"),
    SELF_INSURANCE(2,"自助投保"),;

    private SellingChannel(int value, String description){
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
