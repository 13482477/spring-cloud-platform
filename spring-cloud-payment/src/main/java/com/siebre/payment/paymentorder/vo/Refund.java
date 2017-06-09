package com.siebre.payment.paymentorder.vo;

import java.io.Serializable;

/**
 * @author Huang Tianci
 */
public class Refund implements Serializable {

    private String orderNumber;

    private String refundNumber;

    private String channelName;

    private String refundStatus;

    private String orderRefundStatus;

    private String createDate;

    private String orderAmount;

    private String refundAmount;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(String refundNumber) {
        this.refundNumber = refundNumber;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getOrderRefundStatus() {
        return orderRefundStatus;
    }

    public void setOrderRefundStatus(String orderRefundStatus) {
        this.orderRefundStatus = orderRefundStatus;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }
}
