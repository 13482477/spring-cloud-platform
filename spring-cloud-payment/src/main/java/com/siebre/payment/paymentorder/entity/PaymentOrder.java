package com.siebre.payment.paymentorder.entity;

import com.siebre.basic.model.BaseObject;
import com.siebre.payment.entity.enums.*;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 支付收单
 * @author lizhiqiang
 */
public class PaymentOrder extends BaseObject {

    private static final long serialVersionUID = 9097516995785709378L;

    private String orderNumber;

    private BigDecimal totalInsuredAmount;

    private BigDecimal totalPremium;

    private BigDecimal amount;//订单支付金额

    private Integer businessPlantform;

    private Integer paymentClient;

    private String returnTradeNo;

    private Date createTime;

    private Integer paymentTerminalType;

    /**
     * 用户可以先选择支付方式，该字段用来保存支付方式的code
     */
    private String paymentWayCode;

    //销售渠道
    private SellingChannel sellingChannel;

    /**
     * 统一支付接口2.0字段
     * ------start-------
     */
    //前端唯一标识一次提交订单的字段
    private String messageId;
    private String notificationUrl;
    //外部订单交易编号
    private String externalOrderNumber;
    private PaymentMethod paymentMethod;
    private PaymentProvider paymentProvider;
    private Long paymentChannelId;

    private PaymentChannel paymentChannel;
    private String currency;
    //账户信息
    private Long paymentAccountId;
    private String summary;

    //-------finish---------

    //订单支付状态
    private PaymentOrderPayStatus status;

    //订单锁定状态
    private PaymentOrderLockStatus lockStatus;

    //退款状态
    @Deprecated
    private PaymentOrderRefundStatus refundStatus;

    //退款金额
    private BigDecimal refundAmount;

    /**
     * 对账状态
     */
    private PaymentOrderCheckStatus checkStatus;

    private Date checkTime;//对账时间

    private List<PaymentOrderItem> items = new ArrayList<PaymentOrderItem>();

    private List<PaymentTransaction> paymentTransactions = new ArrayList<PaymentTransaction>();

    public SellingChannel getSellingChannel() {
        return sellingChannel;
    }

    public void setSellingChannel(SellingChannel sellingChannel) {
        this.sellingChannel = sellingChannel;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public PaymentOrderCheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(PaymentOrderCheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public BigDecimal getTotalInsuredAmount() {
        return totalInsuredAmount;
    }

    public void setTotalInsuredAmount(BigDecimal totalInsuredAmount) {
        this.totalInsuredAmount = totalInsuredAmount;
    }

    public BigDecimal getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(BigDecimal totalPremium) {
        this.totalPremium = totalPremium;
    }

    public Integer getBusinessPlantform() {
        return businessPlantform;
    }

    public void setBusinessPlantform(Integer businessPlantform) {
        this.businessPlantform = businessPlantform;
    }

    public Integer getPaymentClient() {
        return paymentClient;
    }

    public void setPaymentClient(Integer paymentClient) {
        this.paymentClient = paymentClient;
    }

    public String getReturnTradeNo() {
        return returnTradeNo;
    }

    public void setReturnTradeNo(String returnTradeNo) {
        this.returnTradeNo = returnTradeNo == null ? null : returnTradeNo.trim();
    }

    public PaymentOrderPayStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentOrderPayStatus status) {
        this.status = status;
    }

    public Integer getPaymentTerminalType() {
        return paymentTerminalType;
    }

    public void setPaymentTerminalType(Integer paymentTerminalType) {
        this.paymentTerminalType = paymentTerminalType;
    }

    public List<PaymentOrderItem> getItems() {
        return items;
    }

    public void setItems(List<PaymentOrderItem> items) {
        this.items = items;
    }

    public List<PaymentTransaction> getPaymentTransactions() {
        return paymentTransactions;
    }

    public void setPaymentTransactions(List<PaymentTransaction> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }

    @Deprecated
    public PaymentOrderRefundStatus getRefundStatus() {
        return refundStatus;
    }

    @Deprecated
    public void setRefundStatus(PaymentOrderRefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Long getPaymentChannelId() {
        return paymentChannelId;
    }

    public void setPaymentChannelId(Long paymentChannelId) {
        this.paymentChannelId = paymentChannelId;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public PaymentChannel getPaymentChannel() {
        return paymentChannel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPaymentWayCode() {
        return paymentWayCode;
    }

    public void setPaymentWayCode(String paymentWayCode) {
        this.paymentWayCode = paymentWayCode;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Long getPaymentAccountId() {
        return paymentAccountId;
    }

    public void setPaymentAccountId(Long paymentAccountId) {
        this.paymentAccountId = paymentAccountId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentProvider getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    public String getExternalOrderNumber() {
        return externalOrderNumber;
    }

    public void setExternalOrderNumber(String externalOrderNumber) {
        this.externalOrderNumber = externalOrderNumber;
    }

    public PaymentOrderLockStatus getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(PaymentOrderLockStatus lockStatus) {
        this.lockStatus = lockStatus;
    }

    @Override
    public String toString() {
        return "PaymentOrder{" +
                "orderNumber='" + orderNumber + '\'' +
                ", totalInsuredAmount=" + totalInsuredAmount +
                ", totalPremium=" + totalPremium +
                ", businessPlantform=" + businessPlantform +
                ", paymentClient=" + paymentClient +
                ", returnTradeNo='" + returnTradeNo + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", paymentTerminalType=" + paymentTerminalType +
                ", paymentChannelId='" + paymentChannelId + '\'' +
                ", paymentChannel=" + paymentChannel +
                ", checkStatus=" + checkStatus +
                ", refundStatus=" + refundStatus +
                ", refundAmount=" + refundAmount +
                ", items=" + items +
                ", paymentTransactions=" + paymentTransactions +
                '}';
    }
}