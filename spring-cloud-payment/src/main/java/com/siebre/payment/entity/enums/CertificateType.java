package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * @author Huang Tianci
 * 证件类型
 */
public enum CertificateType implements BaseEnum {
    ID_CARD(1,"第二代居民身份证"),
    ;

    public static CertificateType valueOf(int value){
        switch (value) {
            case 1:
                return ID_CARD;
            default:
                return null;
        }
    }

    CertificateType(int value, String description) {
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
