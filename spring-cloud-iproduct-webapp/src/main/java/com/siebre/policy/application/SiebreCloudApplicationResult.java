package com.siebre.policy.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.siebre.agreement.Agreement;
import com.siebre.agreement.AgreementRequestResult;
import com.siebre.agreement.dto.ComponentData;
import com.siebre.agreement.dto.support.AgreementConverter;
import com.siebre.policy.InsurancePolicy;
import com.siebre.policy.application.Exception.SiebreCloudAgreementValidationError;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Created by huangfei on 2017/06/27.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SiebreCloudApplicationResult {

    private String applicationNumber;

    private BigDecimal grossPremium;

    private ComponentData policy;

    private int resultCode; //This can be replaced with HTTP response status code.

    private List<SiebreCloudAgreementValidationError> message;

    public SiebreCloudApplicationResult(Agreement agreement, List<SiebreCloudAgreementValidationError> errors) {
        if (agreement != null){
            AgreementConverter converter = new AgreementConverter(agreement);
            policy = converter.covert();
        }
        this.message = errors;
    }

    public SiebreCloudApplicationResult(AgreementRequestResult requestResult) {
        InsurancePolicy policy = (InsurancePolicy) requestResult.getAgreement();
        this.applicationNumber = UUID.randomUUID().toString();
        this.grossPremium = policy.getGrossPremium();
        //AgreementConverter converter = new AgreementConverter(agreement);
        //policy = converter.covert();
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<SiebreCloudAgreementValidationError> getMessage() {
        return message;
    }

    public void setMessage(List<SiebreCloudAgreementValidationError> message) {
        this.message = message;
    }

    public ComponentData getPolicy() {
        return policy;
    }

    public void setPolicy(ComponentData policy) {
        this.policy = policy;
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
}
