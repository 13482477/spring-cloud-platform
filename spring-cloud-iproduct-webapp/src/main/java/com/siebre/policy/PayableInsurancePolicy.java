package com.siebre.policy;

import com.siebre.policy.InsurancePolicy;

/**
 * Created by meilan on 2017/7/12.
 */
public interface PayableInsurancePolicy extends InsurancePolicy {

    String getPaymentNumber();

    void setPaymentNumber(String paymentNumber);
}
