package com.siebre.policy.payment.service;

import com.siebre.policy.application.Exception.SiebreCloudAgreementValidationError;
import com.siebre.policy.application.SiebreCloudApplicationResult;
import com.siebre.policy.dao.InsurancePolicyRepository;
import com.siebre.policy.PayableInsurancePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by meilan on 2017/7/12.
 */
@Service
public class SiebreCloudPaymentService {

    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;

    public SiebreCloudApplicationResult startPay(Map<String,Object> properties) {

        PayableInsurancePolicy policy = (PayableInsurancePolicy)insurancePolicyRepository.findByApplicationNumber((String) properties.get("applicationNumber"));
        if (properties.get("amount") != policy.getGrossPremium()) {
            return returnErrorResult("确认保单金额与保费金额不一致");
        }

        //根据paymentNumber查ipay保单支付状态


        return null;
    }

    public SiebreCloudApplicationResult returnErrorResult(String errorMessage) {
        SiebreCloudApplicationResult result = new SiebreCloudApplicationResult(null,null);
        List<SiebreCloudAgreementValidationError> errors = new ArrayList<SiebreCloudAgreementValidationError>();
        SiebreCloudAgreementValidationError error = new SiebreCloudAgreementValidationError(null,null,null);
        error.setDescription(errorMessage);
        errors.add(error);
        result.setMessage(errors);
        result.setResultCode(400);
        return result;
    }


}
