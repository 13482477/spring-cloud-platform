package com.siebre.policy.application;

import com.siebre.agreement.Agreement;
import com.siebre.agreement.AgreementRequestResult;
import com.siebre.agreement.validation.AgreementValidationError;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huangfei on 2017/06/27.
 */
public class SiebreCloudApplicationResult {

    protected String applicationNumber;

    protected BigDecimal grossPremium;

    public SiebreCloudApplicationResult(Agreement agreement, List<AgreementValidationError> errors) {
//        super(agreement, errors);
        this.applicationNumber = "A0011";
        this.grossPremium = (BigDecimal) agreement.getSmfProperty("grossPremium");
    }

    public SiebreCloudApplicationResult(AgreementRequestResult requestResult) {
//        super(requestResult);
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
}
