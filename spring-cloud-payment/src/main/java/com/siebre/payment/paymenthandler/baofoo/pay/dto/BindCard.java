package com.siebre.payment.paymenthandler.baofoo.pay.dto;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class BindCard {
    //绑定id
    private String bindId;

    //绑定卡号
    private String cardNumber;

    //银行代码
    private String bankCode;

    //银行名称
    private String bankName;

    //银行卡类型
    private String cardType;

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
