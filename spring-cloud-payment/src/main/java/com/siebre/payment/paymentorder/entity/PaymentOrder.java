package com.siebre.payment.paymentorder.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.siebre.basic.model.BaseObject;
import com.siebre.payment.entity.enums.PaymentOrderCheckStatus;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentOrderRefundStatus;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;

/**
 * 支付收单
 * @author lizhiqiang
 */
public class PaymentOrder extends BaseObject {

	private static final long serialVersionUID = 9097516995785709378L;

	private String orderNumber;

    private BigDecimal totalInsuredAmount;

    private BigDecimal totalPremium;

    private Integer businessPlantform;

    private Integer paymentClient;

    private String returnTradeNo;

    private PaymentOrderPayStatus status;

    private Date createTime;

    private Integer paymentTerminalType;

    private Long paymentChannelId;

    private PaymentChannel paymentChannel;

    /**
     * 用户可以先选择支付方式，该字段用来保存支付方式的code
     */
    private String paymentWayCode;

    /**
     * 对账状态
     */
    private PaymentOrderCheckStatus checkStatus;

    //退款状态
    private PaymentOrderRefundStatus refundStatus;

    //退款金额
    private BigDecimal refundAmount;

    private List<PaymentOrderItem> paymentOrderItems = new ArrayList<PaymentOrderItem>();
    
    private List<PaymentTransaction> paymentTransactions = new ArrayList<PaymentTransaction>();

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

	public List<PaymentOrderItem> getPaymentOrderItems() {
		return paymentOrderItems;
	}

	public void setPaymentOrderItems(List<PaymentOrderItem> paymentOrderItems) {
		this.paymentOrderItems = paymentOrderItems;
	}

	public List<PaymentTransaction> getPaymentTransactions() {
		return paymentTransactions;
	}

	public void setPaymentTransactions(List<PaymentTransaction> paymentTransactions) {
		this.paymentTransactions = paymentTransactions;
	}

    public PaymentOrderRefundStatus getRefundStatus() {
        return refundStatus;
    }

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
                ", paymentOrderItems=" + paymentOrderItems +
                ", paymentTransactions=" + paymentTransactions +
                '}';
    }
}