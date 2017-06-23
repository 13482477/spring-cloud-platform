package com.siebre.payment.paymentgateway.vo;

import com.siebre.payment.policyrole.entity.PolicyRole;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Huang Tianci
 *         统一支付接口，保单信息模型
 */
public class UnifiedPayItem implements Serializable {

    @ApiModelProperty(value = "投保单号", required = true)
    private String applicationNumber;

    @ApiModelProperty(value = "保单号", required = true)
    private String policyNumber;

    @ApiModelProperty(value = "保费", required = true)
    private BigDecimal grossPremium;

    @ApiModelProperty(value = "投保人", required = false)
    private PolicyRole applicant;

    @ApiModelProperty(value = "被保人", required = true)
    private PolicyRole insured;

    @ApiModelProperty(value = "产品名称", required = true)
    private String productName;

    @ApiModelProperty(value = "产品代码", required = true)
    private String productCode;

    @ApiModelProperty(value = "起保日", required = true)
    private Date inceptionDate;

    @ApiModelProperty(value = "结束日期", required = true)
    private Date plannedEndDate;

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

    public BigDecimal getGrossPremium() {
        return grossPremium;
    }

    public void setGrossPremium(BigDecimal grossPremium) {
        this.grossPremium = grossPremium;
    }

    public PolicyRole getApplicant() {
        return applicant;
    }

    public void setApplicant(PolicyRole applicant) {
        this.applicant = applicant;
    }

    public PolicyRole getInsured() {
        return insured;
    }

    public void setInsured(PolicyRole insured) {
        this.insured = insured;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
