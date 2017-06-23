package com.siebre.payment.paymentaccount.entity;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 支付宝账户
 */
public class AliPayAccount implements Serializable {

    private Long id;

    private String accountNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
