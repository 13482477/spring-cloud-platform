package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * @author Huang Tianci
 * 订单锁定状态
 */
public enum PaymentOrderLockStatus implements BaseEnum {

    UNLOCK(1, "未锁定"),

    LOCK(2, "锁定"),
    ;

    private PaymentOrderLockStatus(int value, String description) {
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
