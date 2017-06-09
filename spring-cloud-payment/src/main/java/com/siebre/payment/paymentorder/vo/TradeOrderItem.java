package com.siebre.payment.paymentorder.vo;

/** 交易订单条目-(保单信息)
 * Created by AdamTang on 2017/3/23.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class TradeOrderItem {
    /**
     * item号
     */
    private String itemNumber;
    /**
     * 投保单号
     */
    private String applicationNumber;
    /**
     * 保单号
     */
    private String policyNumber;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 保单状态
     */
    private String policyState;

    /**
     * 投保人名称
     */
    private String applicantName;

    /**
     * 投保人手机号
     */
    private String applicantMobile;

    /**
     * 投保人证件类型
     */
    private String applicantIdentityType;

    /**
     * 投保人证件号码
     */
    private String applicantIdentityNumber;

    /**
     * 投保人开户银行
     */
    private String applicantPayBank;

    /**
     * 投保人银行号码
     */
    private String applicantPayBankNumber;

    /**
     * 支付金额
     */
    private String payAmount;

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPolicyState() {
        return policyState;
    }

    public void setPolicyState(String policyState) {
        this.policyState = policyState;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantMobile() {
        return applicantMobile;
    }

    public void setApplicantMobile(String applicantMobile) {
        this.applicantMobile = applicantMobile;
    }

    public String getApplicantIdentityType() {
        return applicantIdentityType;
    }

    public void setApplicantIdentityType(String applicantIdentityType) {
        this.applicantIdentityType = applicantIdentityType;
    }

    public String getApplicantIdentityNumber() {
        return applicantIdentityNumber;
    }

    public void setApplicantIdentityNumber(String applicantIdentityNumber) {
        this.applicantIdentityNumber = applicantIdentityNumber;
    }

    public String getApplicantPayBank() {
        return applicantPayBank;
    }

    public void setApplicantPayBank(String applicantPayBank) {
        this.applicantPayBank = applicantPayBank;
    }

    public String getApplicantPayBankNumber() {
        return applicantPayBankNumber;
    }

    public void setApplicantPayBankNumber(String applicantPayBankNumber) {
        this.applicantPayBankNumber = applicantPayBankNumber;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }
}

