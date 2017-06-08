package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * Payment methods supported by payment paymentProvider. There is a relationship between paymentProvider and paymentMethod.
 */
public enum PaymentMethod implements BaseEnum {

    PUBLIC_ACCOUNT_PAY(1, "公众号支付"),

    NET_GATEWAY(2, "网关支付"),
    ;

    public static PaymentMethod valueOf(int value){
        switch (value) {
            case 1:
                return PUBLIC_ACCOUNT_PAY;
            case 2:
                return NET_GATEWAY;
            default:
                return null;
        }
    }

    private PaymentMethod(int value, String description) {
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
