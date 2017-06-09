package com.siebre.payment.paymentcheck.vo;

import com.siebre.payment.entity.enums.PaymentOrderCheckStatus;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;

import java.math.BigDecimal;
import java.util.Date;

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
    private PaymentOrderCheckStatus checkStatus;

    /**
     * 对账时间
     */
    private Date checkTime;

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
    private Date applicationCreateTime;

    //投保信息-保单状态
    private PaymentOrderPayStatus applicationPayStatus;

    //第三方-外部交易流水号
    private String externalTransactionNumber;

    //第三方-支付时间
    private Date payTime;

    //第三方-实际支付
    private BigDecimal realAmount;

    //第三方-支付状态
    private PaymentOrderPayStatus realPayStatus;

    /**
     * 支付信息-订单状态
     */
    private PaymentOrderPayStatus payStatus;

    /**
     * 支付信息-创建时间
     */
    private Date createTime;

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

    public PaymentOrderCheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(PaymentOrderCheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
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

    public Date getApplicationCreateTime() {
        return applicationCreateTime;
    }

    public void setApplicationCreateTime(Date applicationCreateTime) {
        this.applicationCreateTime = applicationCreateTime;
    }

    public PaymentOrderPayStatus getApplicationPayStatus() {
        return applicationPayStatus;
    }

    public void setApplicationPayStatus(PaymentOrderPayStatus applicationPayStatus) {
        this.applicationPayStatus = applicationPayStatus;
    }

    public String getExternalTransactionNumber() {
        return externalTransactionNumber;
    }

    public void setExternalTransactionNumber(String externalTransactionNumber) {
        this.externalTransactionNumber = externalTransactionNumber;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public PaymentOrderPayStatus getRealPayStatus() {
        return realPayStatus;
    }

    public void setRealPayStatus(PaymentOrderPayStatus realPayStatus) {
        this.realPayStatus = realPayStatus;
    }

    public PaymentOrderPayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PaymentOrderPayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
