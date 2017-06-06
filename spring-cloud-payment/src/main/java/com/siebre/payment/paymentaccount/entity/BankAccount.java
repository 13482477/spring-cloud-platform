package com.siebre.payment.paymentaccount.entity;

import com.siebre.payment.entity.enums.BankAccountType;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 银行卡类型账户
 */
public class BankAccount implements Serializable {

    private Long id;

    //银行卡类型
    private BankAccountType accountType;

    private String acountNumber;

    private String bankCode;

    private String holderName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
