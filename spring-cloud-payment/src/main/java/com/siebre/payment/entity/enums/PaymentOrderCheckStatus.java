package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * Created by AdamTang on 2017/3/31.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public enum PaymentOrderCheckStatus implements BaseEnum {
    NOT_CONFIRM(1, "未对账"),

    SUCCESS(2, "文件对账成功"),

    FAIL(3, "文件对账失败"),

    UNUSUAL(4, "对账异常"),

    REAL_TIME_SUCCESS(5, "实时对账成功"),

    REAL_TIME_FAIL(6, "实时对账失败"),
    ;

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
