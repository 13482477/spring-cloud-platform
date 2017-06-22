package com.siebre.payment.paymenttransaction.vo;

import com.siebre.payment.entity.enums.PaymentTransactionStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tianci.huang on 2017/6/20.
 */
public class TransactionVo implements Serializable {

    private Long transactionId;

    private String paymentStatus;

    private BigDecimal paymentAmount;

    private String internalTransactionNumber;

    private String createDate;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getInternalTransactionNumber() {
        return internalTransactionNumber;
    }

    public void setInternalTransactionNumber(String internalTransactionNumber) {
        this.internalTransactionNumber = internalTransactionNumber;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
