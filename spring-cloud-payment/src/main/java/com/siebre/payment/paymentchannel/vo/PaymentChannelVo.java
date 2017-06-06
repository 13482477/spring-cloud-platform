package com.siebre.payment.paymentchannel.vo;


import com.siebre.payment.entity.enums.PaymentChannelStatus;

import java.io.Serializable;

public class PaymentChannelVo implements Serializable {

    private String channelCode;

    private String channelName;

    private PaymentChannelStatus status;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public PaymentChannelStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentChannelStatus status) {
        this.status = status;
    }
}
