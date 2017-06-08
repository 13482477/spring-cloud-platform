package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * The system, platform or orgnization that will ultimately deal with paying with money.
 */
public enum PaymentProvider implements BaseEnum {

    WECHAT(1, "微信"),

    ALIPAY(2, "支付宝"),

    ALLINPAY(3, "通联"),

    BAOFOO(4, "宝付"),
    ;

    public static PaymentProvider valueOf(int value){
        switch (value) {
            case 1:
                return WECHAT;
            case 2:
                return ALIPAY;
            case 3:
                return ALLINPAY;
            case 4:
                return BAOFOO;
            default:
                return null;
        }
    }

    PaymentProvider(int value, String description) {
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
