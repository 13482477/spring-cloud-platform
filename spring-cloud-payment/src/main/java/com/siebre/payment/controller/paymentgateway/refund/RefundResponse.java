package com.siebre.payment.controller.paymentgateway.refund;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class RefundResponse implements Serializable {

	private static final long serialVersionUID = -5752076769574098065L;

	private String refundStatus;

    private String applicationNumber;

    private String response;

    private BigDecimal refundAmount;

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }


}
