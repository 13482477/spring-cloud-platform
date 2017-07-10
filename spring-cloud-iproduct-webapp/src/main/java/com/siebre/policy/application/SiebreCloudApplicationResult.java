package com.siebre.policy.application;

import com.siebre.agreement.Agreement;
import com.siebre.agreement.AgreementRequestResult;
import com.siebre.agreement.dto.ComponentData;
import com.siebre.agreement.dto.support.AgreementConverter;
import com.siebre.policy.application.Exception.SiebreCloudAgreementValidationError;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huangfei on 2017/06/27.
 */
public class SiebreCloudApplicationResult {

    private String applicationNumber;

    private BigDecimal grossPremium;

    private ComponentData policy;

    private Boolean underwritingResult; //This can be replaced with HTTP response status code.

    private List<SiebreCloudAgreementValidationError> errors;

    public SiebreCloudApplicationResult(Agreement agreement, List<SiebreCloudAgreementValidationError> errors) {
        this.applicationNumber = "A0011";
        this.grossPremium = (BigDecimal) agreement.getSmfProperty("grossPremium");
        this.errors = errors;

        AgreementConverter converter = new AgreementConverter(agreement);
        policy = converter.covert();
    }

    public SiebreCloudApplicationResult(AgreementRequestResult requestResult) {
        Agreement agreement = requestResult.getAgreement();
        this.applicationNumber = "A0011";
        this.grossPremium = (BigDecimal) agreement.getSmfProperty("grossPremium");

        AgreementConverter converter = new AgreementConverter(agreement);
        policy = converter.covert();
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

    public ComponentData getPolicy() {
        return policy;
    }

    public void setPolicy(ComponentData policy) {
        this.policy = policy;
    }
}
