package com.siebre.payment.billing.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ReconJobInstance {
    private Long id;

    private Long reconJobId;

    private String channelCode;

    private Date transDate;

    private String transNo;

    private Date reconcileTime;

    private BigDecimal totalAmount;

    private Integer transCount;

    private Integer matchedCount;

    private BigDecimal payTotalAmount;

    private Integer payTransCount;

    private BigDecimal refundTotalAmount;

    private Integer refundTransCount;

    private String responseSignType;

    private String mchType;

    private String reconcileStatus;

    private String reconcileStatusMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReconJobId() {
        return reconJobId;
    }

    public void setReconJobId(Long reconJobId) {
        this.reconJobId = reconJobId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo == null ? null : transNo.trim();
    }

    public Date getReconcileTime() {
        return reconcileTime;
    }

    public void setReconcileTime(Date reconcileTime) {
        this.reconcileTime = reconcileTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTransCount() {
        return transCount;
    }

    public void setTransCount(Integer transCount) {
        this.transCount = transCount;
    }

    public Integer getMatchedCount() {
        return matchedCount;
    }

    public void setMatchedCount(Integer matchedCount) {
        this.matchedCount = matchedCount;
    }

    public BigDecimal getPayTotalAmount() {
        return payTotalAmount;
    }

    public void setPayTotalAmount(BigDecimal payTotalAmount) {
        this.payTotalAmount = payTotalAmount;
    }

    public Integer getPayTransCount() {
        return payTransCount;
    }

    public void setPayTransCount(Integer payTransCount) {
        this.payTransCount = payTransCount;
    }

    public BigDecimal getRefundTotalAmount() {
        return refundTotalAmount;
    }

    public void setRefundTotalAmount(BigDecimal refundTotalAmount) {
        this.refundTotalAmount = refundTotalAmount;
    }

    public Integer getRefundTransCount() {
        return refundTransCount;
    }

    public void setRefundTransCount(Integer refundTransCount) {
        this.refundTransCount = refundTransCount;
    }

    public String getResponseSignType() {
        return responseSignType;
    }

    public void setResponseSignType(String responseSignType) {
        this.responseSignType = responseSignType == null ? null : responseSignType.trim();
    }

    public String getMchType() {
        return mchType;
    }

    public void setMchType(String mchType) {
        this.mchType = mchType == null ? null : mchType.trim();
    }

    public String getReconcileStatus() {
        return reconcileStatus;
    }

    public void setReconcileStatus(String reconcileStatus) {
        this.reconcileStatus = reconcileStatus == null ? null : reconcileStatus.trim();
    }

    public String getReconcileStatusMessage() {
        return reconcileStatusMessage;
    }

    public void setReconcileStatusMessage(String reconcileStatusMessage) {
        this.reconcileStatusMessage = reconcileStatusMessage == null ? null : reconcileStatusMessage.trim();
    }
}