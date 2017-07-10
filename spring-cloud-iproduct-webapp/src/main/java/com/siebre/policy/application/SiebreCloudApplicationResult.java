package com.siebre.policy.application;

import com.siebre.agreement.Agreement;
import com.siebre.agreement.AgreementRequestResult;
import com.siebre.agreement.validation.AgreementValidationError;
import com.siebre.policy.application.Exception.SiebreCloudAgreementValidationError;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huangfei on 2017/06/27.
 */
public class SiebreCloudApplicationResult {

    private String applicationNumber;

    private BigDecimal grossPremium;

    private Boolean underwritingResult;

    private List<SiebreCloudAgreementValidationError> errors;

    public SiebreCloudApplicationResult(Agreement agreement, List<SiebreCloudAgreementValidationError> errors) {
        this.applicationNumber = "A0011";
        this.grossPremium = (BigDecimal) agreement.getSmfProperty("grossPremium");
        this.errors = errors;
    }

    public SiebreCloudApplicationResult(AgreementRequestResult requestResult) {
        Agreement agreement = requestResult.getAgreement();
        this.applicationNumber = "A0011";
        this.grossPremium = (BigDecimal) agreement.getSmfProperty("grossPremium");
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public BigDecimal getGrossPremium() {
        return grossPremium;
    }

    public void setGrossPremium(BigDecimal grossPremium) {
        this.grossPremium = grossPremium;
    }

    public List<SiebreCloudAgreementValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<SiebreCloudAgreementValidationError> errors) {
        this.errors = errors;
    }

    public Boolean getUnderwritingResult() {
        return underwritingResult;
    }

    public void setUnderwritingResult(Boolean underwritingResult) {
        this.underwritingResult = underwritingResult;
    }
}
