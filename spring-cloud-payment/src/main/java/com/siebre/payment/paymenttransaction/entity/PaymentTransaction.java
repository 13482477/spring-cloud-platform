package com.siebre.payment.paymenttransaction.entity;

import com.siebre.basic.model.BaseObject;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentway.entity.PaymentWay;

import java.math.BigDecimal;

/**
 * 支付-交易，不仅仅是支付类型的交易
 * 包含退款，查询，等交易
 */
public class PaymentTransaction extends BaseObject {

	private static final long serialVersionUID = 1887250643077979518L;

	private Long paymentChannelId;
	
	private PaymentChannel paymentChannel;

    private Long paymentWayId;
    
    private PaymentWay paymentWay;

    private Long paymentOrderId;
    
    private PaymentOrder paymentOrder;

    private String internalTransactionNumber;

    private String externalTransactionNumber;

    private BigDecimal paymentAmount;

    /**
     * 交易状态
     */
    private PaymentTransactionStatus paymentStatus;

    /**
     * 交易类型，通过交易类型区分paymentTransaction
     */
    private PaymentInterfaceType interfaceType;

    private String transactionMessageId;

    private String payeeAccount;

    private String paymentAccout;

    /**
     * 发送方
     */
    private String sender;

    /**
     * 接收方
     */
    private String receiver;

    public Long getPaymentChannelId() {
        return paymentChannelId;
    }

    public void setPaymentChannelId(Long paymentChannelId) {
        this.paymentChannelId = paymentChannelId;
    }

    public Long getPaymentWayId() {
        return paymentWayId;
    }

    public void setPaymentWayId(Long paymentWayId) {
        this.paymentWayId = paymentWayId;
    }

    public Long getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(Long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getInternalTransactionNumber() {
        return internalTransactionNumber;
    }

    public void setInternalTransactionNumber(String internalTransactionNumber) {
        this.internalTransactionNumber = internalTransactionNumber == null ? null : internalTransactionNumber.trim();
    }

    public String getExternalTransactionNumber() {
        return externalTransactionNumber;
    }

    public void setExternalTransactionNumber(String externalTransactionNumber) {
        this.externalTransactionNumber = externalTransactionNumber == null ? null : externalTransactionNumber.trim();
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

	public PaymentTransactionStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentTransactionStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getTransactionMessageId() {
        return transactionMessageId;
    }

    public void setTransactionMessageId(String transactionMessageId) {
        this.transactionMessageId = transactionMessageId == null ? null : transactionMessageId.trim();
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount == null ? null : payeeAccount.trim();
    }

    public String getPaymentAccout() {
        return paymentAccout;
    }

    public void setPaymentAccout(String paymentAccout) {
        this.paymentAccout = paymentAccout == null ? null : paymentAccout.trim();
    }

	public PaymentChannel getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public PaymentWay getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(PaymentWay paymentWay) {
		this.paymentWay = paymentWay;
	}

	public PaymentOrder getPaymentOrder() {
		return paymentOrder;
	}

	public void setPaymentOrder(PaymentOrder paymentOrder) {
		this.paymentOrder = paymentOrder;
	}

    public PaymentInterfaceType getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(PaymentInterfaceType interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}