package com.siebre.payment.entity.transactionmessage;

import com.siebre.basic.model.BaseObject;

public class TransactionMessage extends BaseObject {

	private static final long serialVersionUID = -3659751520344237223L;

    private Long paymentChannelId;

    private Long paymentInterfaceId;

    private Integer messageType;

    private String data;

    public Long getPaymentChannelId() {
        return paymentChannelId;
    }

    public void setPaymentChannelId(Long paymentChannelId) {
        this.paymentChannelId = paymentChannelId;
    }

    public Long getPaymentInterfaceId() {
        return paymentInterfaceId;
    }

    public void setPaymentInterfaceId(Long paymentInterfaceId) {
        this.paymentInterfaceId = paymentInterfaceId;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }
}