package com.siebre.payment.paymentorder.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeOrder implements Serializable {

    private Long orderId;

    /**
     * 订单信息 order info
     */
    @ApiModelProperty(value = "订单号", required = false)
    private String orderNumber;//订单号
    @ApiModelProperty(value = "创建时间", required = false)
    private String orderCreateDate;//创建时间
    @ApiModelProperty(value = "交易状态", required = false)
    private String orderState;//交易状态   对应order上的status
    @ApiModelProperty(value = "订单金额(元)", required = false)
    private BigDecimal orderAmount;//订单金额(元)
    @ApiModelProperty(value = "销售渠道", required = false)
    private String saleChannel;//销售渠道

    /**
     * 保单摘要
     */
    @ApiModelProperty(value = "产品名称", required = false)
    private String productName;//产品名称
    @ApiModelProperty(value = "投保单号", required = false)
    private String applicationNumber;//投保单号
    @ApiModelProperty(value = "保单号", required = false)
    private String policyNumber;//保单号
    @ApiModelProperty(value = "投保人", required = false)
    private String application;//投保人
    @ApiModelProperty(value = "手机号", required = false)
    private String phoneNumber;//手机号
    @ApiModelProperty(value = "证件类型", required = false)
    private String idType;//证件类型
    @ApiModelProperty(value = "证件号", required = false)
    private String idNumber;//证件号


    /**
     * 支付信息
     */
    @ApiModelProperty(value = "支付渠道", required = false)
    private String payChannel;//支付渠道
    @ApiModelProperty(value = "支付时间", required = false)
    private String payTime;//支付时间   对应order上的payTime
    @ApiModelProperty(value = "支付方式", required = false)
    private String payWay;//支付方式
    @ApiModelProperty(value = "支付金额", required = false)
    private BigDecimal payAmount;//支付金额(元)
    @ApiModelProperty(value = "支付状态", required = false)
    private String payStatus;//支付状态   对应transaction中的status

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(String orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getSaleChannel() {
        return saleChannel;
    }

    public void setSaleChannel(String saleChannel) {
        this.saleChannel = saleChannel;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
