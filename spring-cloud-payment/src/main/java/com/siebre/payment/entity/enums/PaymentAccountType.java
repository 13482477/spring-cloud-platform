package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * @author Huang Tianci
 * 账户类型
 */
public enum PaymentAccountType implements BaseEnum {

    BANK_ACCOUNT(1, "银行卡"),

    WECHAT_ACCOUNT(2, "微信"),

    ALIPAY_ACCOUNT(3, "支付宝"),
    ;

    public static PaymentAccountType valueOf(int value){
        switch (value) {
            case 1:
                return BANK_ACCOUNT;
            case 2:
                return WECHAT_ACCOUNT;
            default:
                return null;
        }
    }

    private PaymentAccountType(int value, String description) {
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
