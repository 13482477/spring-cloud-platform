package com.siebre.payment.paymenttransaction.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianci.huang on 2017/6/23.
 */
public class RefundDetail implements Serializable {

    private String orderNumber;

    private String refundApplicationNumber;

    private String channel;

    private BigDecimal refundAmount;

    private String refundStatus;

    private List<RefundRecord> refundRecords = new ArrayList<>();

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRefundApplicationNumber() {
        return refundApplicationNumber;
    }

    public void setRefundApplicationNumber(String refundApplicationNumber) {
        this.refundApplicationNumber = refundApplicationNumber;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public List<RefundRecord> getRefundRecords() {
        return refundRecords;
    }

    public void setRefundRecords(List<RefundRecord> refundRecords) {
        this.refundRecords = refundRecords;
    }
}
