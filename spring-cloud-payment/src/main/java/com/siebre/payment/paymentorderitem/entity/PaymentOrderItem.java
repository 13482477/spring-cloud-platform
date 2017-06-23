package com.siebre.payment.paymentorderitem.entity;

import com.siebre.basic.model.BaseObject;
import com.siebre.payment.policylibility.entity.PolicyLibility;
import com.siebre.payment.policyrole.entity.PolicyRole;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentOrderItem extends BaseObject {

	private static final long serialVersionUID = -4206852760660778119L;

	private Long paymentOrderId;

	/**
	 * 投保单号
	 */
    private String applicationNumber;

    /**
     * 保单号
     */
    private String policyNumber;

    /**
     * 保费
     */
    private BigDecimal grossPremium;

    /**
     * 责任
     * v2.0版本，该模型废弃
     */
    @Deprecated
    private List<PolicyLibility> libilities = new ArrayList<>();

    /**
     * 投保人
     */
    private Long applicantId;
    private PolicyRole applicant;

    /**
     * 被保人
     */
    private Long insuredPersonId;
    private PolicyRole insured;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品代码
     */
    private String productCode;

    private Date inceptionDate;

    private Date plannedEndDate;


    public Long getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(Long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber == null ? null : applicationNumber.trim();
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public BigDecimal getGrossPremium() {
        return grossPremium;
    }

    public void setGrossPremium(BigDecimal grossPremium) {
        this.grossPremium = grossPremium;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<PolicyLibility> getLibilities() {
        return libilities;
    }

    public void setLibilities(List<PolicyLibility> libilities) {
        this.libilities = libilities;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public PolicyRole getApplicant() {
        return applicant;
    }

    public void setApplicant(PolicyRole applicant) {
        this.applicant = applicant;
    }

    public Long getInsuredPersonId() {
        return insuredPersonId;
    }

    public void setInsuredPersonId(Long insuredPersonId) {
        this.insuredPersonId = insuredPersonId;
    }

    public PolicyRole getInsured() {
        return insured;
    }

    public void setInsured(PolicyRole insured) {
        this.insured = insured;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Date getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(Date inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }
}