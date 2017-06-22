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

    private String returnCode;

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
     */
    private PaymentTransaction refundTransaction;

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

    public RefundApplicationStatus getRefundApplicationStatus() {
        return refundApplicationStatus;
    }

    public void setRefundApplicationStatus(RefundApplicationStatus refundApplicationStatus) {
        this.refundApplicationStatus = refundApplicationStatus;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getExternalTransactionNumber() {
        return externalTransactionNumber;
    }

    public void setExternalTransactionNumber(String externalTransactionNumber) {
        this.externalTransactionNumber = externalTransactionNumber;
    }

    public PaymentTransaction getRefundTransaction() {
        return refundTransaction;
    }

    public void setRefundTransaction(PaymentTransaction refundTransaction) {
        this.refundTransaction = refundTransaction;
    }

    public RefundApplication getRefundApplication() {
        return refundApplication;
    }

    public void setRefundApplication(RefundApplication refundApplication) {
        this.refundApplication = refundApplication;
    }
}
