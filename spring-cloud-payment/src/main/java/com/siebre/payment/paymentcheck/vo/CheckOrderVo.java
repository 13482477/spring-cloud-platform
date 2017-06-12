package com.siebre.payment.paymentcheck.vo;

import com.siebre.payment.entity.enums.PaymentOrderCheckStatus;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;

import java.math.BigDecimal;

/**
 * Created by meilan on 2017/6/6.
 * 对账订单模型
 */
public class CheckOrderVo {

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 支付渠道
     */
    private String channelCode;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 对账类型（支付，退款）
     */
    private String checkType;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 对账结果
     */
    private String checkStatus;

    /**
     * 对账时间
     */
    private String checkTime;

    /**
     * 投保信息-投保单号
     */
    private String applicationNumber;

    /**
     * 投保信息-保费金额
     */
    private BigDecimal premium;

    /**
     * 投保信息-投保信息创建时间
     */
    private String applicationCreateTime;

    //投保信息-保单状态
    private String applicationPayStatus;

    //第三方-外部交易流水号
    private String externalTransactionNumber;

    //第三方-支付时间
    private String payTime;

    //第三方-实际支付
    private BigDecimal realAmount;

    //第三方-支付状态
    private String realPayStatus;

    /**
     * 支付信息-订单状态
     */
    private String payStatus;

    /**
     * 支付信息-创建时间
     */
    private String createTime;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public String getExternalTransactionNumber() {
        return externalTransactionNumber;
    }

    public void setExternalTransactionNumber(String externalTransactionNumber) {
        this.externalTransactionNumber = externalTransactionNumber;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getApplicationCreateTime() {
        return applicationCreateTime;
    }

    public void setApplicationCreateTime(String applicationCreateTime) {
        this.applicationCreateTime = applicationCreateTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getApplicationPayStatus() {
        return applicationPayStatus;
    }

    public void setApplicationPayStatus(String applicationPayStatus) {
        this.applicationPayStatus = applicationPayStatus;
    }

    public String getRealPayStatus() {
        return realPayStatus;
    }

    public void setRealPayStatus(String realPayStatus) {
        this.realPayStatus = realPayStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
