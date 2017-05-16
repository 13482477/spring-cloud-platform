package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 退款申请状态
 */
public enum RefundApplicationStatus implements BaseEnum {

    APPLICATION(1,"已申请"),

    SUCCESS(2,"退款成功"),

    FAILED(3,"退款失败"),

    PROCESSING(4,"退款处理中"),

    SUBMITTED(5,"退款申请提交成功")
    ;

    private RefundApplicationStatus(int value, String description) {
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
