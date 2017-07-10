package com.siebre.payment.billing.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ReconItem {
    private Long id;

    //pay_file  refund_file   pay_real_time  refund_real_time
    private String type;

    private Long transId;

    private String reconResult;

    private String orderNumber;

    private String outTradeNo;

    private BigDecimal paymentAmount;

    private BigDecimal refundAmount;

    private Date transTime;

    private Date paySuccessTime;

    private Date refundSuccessTime;

    private String description;

    private String remoteDataSourceJsonStr;

    private String paymentDataSourceJsonStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public String getReconResult() {
        return reconResult;
    }

    public void setReconResult(String reconResult) {
        this.reconResult = reconResult == null ? null : reconResult.trim();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public Date getPaySuccessTime() {
        return paySuccessTime;
    }

    public void setPaySuccessTime(Date paySuccessTime) {
        this.paySuccessTime = paySuccessTime;
    }

    public Date getRefundSuccessTime() {
        return refundSuccessTime;
    }

    public void setRefundSuccessTime(Date refundSuccessTime) {
        this.refundSuccessTime = refundSuccessTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getRemoteDataSourceJsonStr() {
        return remoteDataSourceJsonStr;
    }

    public void setRemoteDataSourceJsonStr(String remoteDataSourceJsonStr) {
        this.remoteDataSourceJsonStr = remoteDataSourceJsonStr == null ? null : remoteDataSourceJsonStr.trim();
    }

    public String getPaymentDataSourceJsonStr() {
        return paymentDataSourceJsonStr;
    }

    public void setPaymentDataSourceJsonStr(String paymentDataSourceJsonStr) {
        this.paymentDataSourceJsonStr = paymentDataSourceJsonStr == null ? null : paymentDataSourceJsonStr.trim();
    }
}