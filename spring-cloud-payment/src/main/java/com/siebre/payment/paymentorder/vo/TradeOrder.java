package com.siebre.payment.paymentorder.vo;

import java.io.Serializable;

/**  交易订单
 * Created by AdamTang on 2017/3/21.
 * Project:siebre-cloud-platform
 * Version:1.0
 */

public class TradeOrder implements Serializable{
    /**
     * 订单号
     */
    private String orderNumber;

    /**
     *订单状态
     */
    private String orderState;

    /**
     *订单状态
     */
    private String payState;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     *支付渠道
     */
    private String payChannel;

    /**
     *支付方式
     */
    private String payMethod;

    /**
     *外部支付流水号
     */
    private String paySerialNumber;
    
    /**
     *创建日期
     */
    private String  orderCreateDate;

    /**
     *订单金额 单位元
     */
    private String orderAmount;

    /**
     *支付金额 单位元
     */
    private String payAmount;

    /**
     * 退款状态
     */
    private String refundStatus;

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPaySerialNumber() {
        return paySerialNumber;
    }

    public void setPaySerialNumber(String paySerialNumber) {
        this.paySerialNumber = paySerialNumber;
    }

    public String getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(String orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }


}
