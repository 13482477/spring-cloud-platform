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
    private String policeNumber;

    /**
     * 保额
     */
    private BigDecimal insuredAmount;

    /**
     * 保费
     */
    private BigDecimal premium;

    /**
     * 保险起期
     */
    private Date startDate;

    /**
     * 保险止期
     */
    private Date endDate;

    /**
     * 责任
     */
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
    private PolicyRole insuredPerson;

    /**
     * 投被保人是否是同一人
     */
    private String samePerson;

    /**
     * 产品名称
     */
    private String productName;

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

    public String getPoliceNumber() {
        return policeNumber;
    }

    public void setPoliceNumber(String policeNumber) {
        this.policeNumber = policeNumber == null ? null : policeNumber.trim();
    }

    public BigDecimal getInsuredAmount() {
        return insuredAmount;
    }

    public void setInsuredAmount(BigDecimal insuredAmount) {
        this.insuredAmount = insuredAmount;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public PolicyRole getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(PolicyRole insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public String getSamePerson() {
        return samePerson;
    }

    public void setSamePerson(String samePerson) {
        this.samePerson = samePerson;
    }

    @Override
    public String toString() {
        return "PaymentOrderItem{" +
                "paymentOrderId=" + paymentOrderId +
                ", applicationNumber='" + applicationNumber + '\'' +
                ", policeNumber='" + policeNumber + '\'' +
                ", insuredAmount=" + insuredAmount +
                ", premium=" + premium +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", libilities=" + libilities +
                ", applicantId=" + applicantId +
                ", applicant=" + applicant +
                ", insuredPersonId=" + insuredPersonId +
                ", insuredPerson=" + insuredPerson +
                ", samePerson='" + samePerson + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}