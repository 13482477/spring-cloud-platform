package com.siebre.policy;

import javax.persistence.Entity;

/**
 * Created by meilan on 2017/7/12.
 */
@Entity
public class PayableInsurancePolicyImpl extends InsurancePolicyImpl implements PayableInsurancePolicy {

    private String paymentNumber;

    public PayableInsurancePolicyImpl() {

    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }
}
