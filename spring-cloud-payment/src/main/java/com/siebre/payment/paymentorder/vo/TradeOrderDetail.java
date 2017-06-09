package com.siebre.payment.paymentorder.vo;


import java.util.ArrayList;
import java.util.List;

/**  交易订单详情
 * Created by AdamTang on 2017/3/21.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class TradeOrderDetail extends TradeOrder{
    /**
     * 手续费类型
     */
    private String serviceChargeType;

    /**
     * 手续费
     */
    private String serviceCharge;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 销售渠道
     */
    private String saleChannel;


    /**
     * 对账状态
     */
    private String reconciliationState;

    /**
     *支付回调时间
     */
    private String payCallBackTime;

    /**
     * 支付确认时间
     */
    private String payConfirmTime;
    /**
     * 订单item详情
     */
    private List<TradeOrderItem> orderItems =new ArrayList<>();

    /**
     * 对账状态
     */
    private String checkStatus;

    /**
     * 交易状态
     */
    private String transactionStatus;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 退款金额
     */
    private String refundAmount;

    /**
     * 退款流水号
     */
    private String refundNumber;

    private String refundTime;

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(String refundNumber) {
        this.refundNumber = refundNumber;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getServiceChargeType() {
        return serviceChargeType;
    }

    public void setServiceChargeType(String serviceChargeType) {
        this.serviceChargeType = serviceChargeType;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public List<TradeOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TradeOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getSaleChannel() {
        return saleChannel;
    }

    public void setSaleChannel(String saleChannel) {
        this.saleChannel = saleChannel;
    }

    public void addOrderItems(TradeOrderItem item){
        this.orderItems.add(item);
    }

    public String getReconciliationState() {
        return reconciliationState;
    }

    public void setReconciliationState(String reconciliationState) {
        this.reconciliationState = reconciliationState;
    }

    public String getPayCallBackTime() {
        return payCallBackTime;
    }

    public void setPayCallBackTime(String payCallBackTime) {
        this.payCallBackTime = payCallBackTime;
    }

    public String getPayConfirmTime() {
        return payConfirmTime;
    }

    public void setPayConfirmTime(String payConfirmTime) {
        this.payConfirmTime = payConfirmTime;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }
}
