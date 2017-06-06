package com.siebre.payment.refundapplication.dto;

import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.refundapplication.entity.RefundApplication;

/**
 * 退款返回信息DTO
 */
public class PaymentRefundResponse {

    /**
     * 退款是否为同步(默认为同步)
     */
    private Boolean synchronize = true;

    /**
     * 退款申请的状态
     */
    private RefundApplicationStatus refundApplicationStatus;

    /**
     * 退款申请的返回信息
     */
    private String returnMessage;

    /**
     * 外部交易流水号
     */
    private String externalTransactionNumber;

    /**
     * 退款交易transaction
     * 冗余设计 同PaymentRefundRequest
     */
    private PaymentTransaction paymentTransaction;

    /**
     * 退款交易申请
     * 冗余设计 同PaymentRefundRequest
     */
    private RefundApplication refundApplication;

    public Boolean getSynchronize() {
        return synchronize;
    }

    public void setSynchronize(Boolean synchronize) {
        this.synchronize = synchronize;
    }

    public String getExternalTransactionNumber() {
        return externalTransactionNumber;
    }

    public void setExternalTransactionNumber(String externalTransactionNumber) {
        this.externalTransactionNumber = externalTransactionNumber;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public RefundApplicationStatus getRefundApplicationStatus() {
        return refundApplicationStatus;
    }

    public void setRefundApplicationStatus(RefundApplicationStatus refundApplicationStatus) {
        this.refundApplicationStatus = refundApplicationStatus;
    }

    public PaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }

    public RefundApplication getRefundApplication() {
        return refundApplication;
    }

    public void setRefundApplication(RefundApplication refundApplication) {
        this.refundApplication = refundApplication;
    }
}
