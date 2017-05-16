package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * Created by AdamTang on 2017/3/31.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public enum PaymentOrderCheckStatus implements BaseEnum {
    NOT_CONFIRM(1,"未对账"),

    SUCCESS(2,"对账成功"),

    FAIL(3,"对账失败"),;

    private PaymentOrderCheckStatus(int value, String description) {

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
