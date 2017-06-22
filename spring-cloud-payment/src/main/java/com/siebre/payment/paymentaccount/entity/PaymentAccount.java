package com.siebre.payment.paymentaccount.entity;

import com.siebre.payment.entity.enums.BankAccountType;
import com.siebre.payment.entity.enums.PaymentAccountType;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 账户信息
 */
public class PaymentAccount implements Serializable{

    private Long id;

    //账户类型
    private PaymentAccountType type;

    //------------------BackAccount-----------------
    //银行卡类型
    private BankAccountType accountType;

    private String acountNumber;   //与支付宝账户共用，详情见AliPayAccount

    private String bankCode;

    private String holderName;

    //-----------------WeChatAcconut-----------------

    private String nickName;

    private String openid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentAccountType getType() {
        return type;
    }

    public void setType(PaymentAccountType type) {
        this.type = type;
    }

    public BankAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(BankAccountType accountType) {
        this.accountType = accountType;
    }

    public String getAcountNumber() {
        return acountNumber;
    }

    public void setAcountNumber(String acountNumber) {
        this.acountNumber = acountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
