package com.siebre.policy.payment.service;

import com.siebre.policy.application.Exception.SiebreCloudAgreementValidationError;
import com.siebre.policy.application.SiebreCloudApplicationResult;
import com.siebre.policy.dao.InsurancePolicyRepository;
import com.siebre.policy.PayableInsurancePolicy;
import com.siebre.policy.enums.PolicyStatus;
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

    public SiebreCloudApplicationResult prePay(Map<String,Object> properties) {

        PayableInsurancePolicy policy = (PayableInsurancePolicy)insurancePolicyRepository.findByApplicationNumber((String) properties.get("applicationNumber"));
        BigDecimal amount = new BigDecimal(properties.get("amount").toString());
        if ( amount.compareTo(policy.getGrossPremium()) != 0) {
            return errorResult("投保单确认金额与保费金额不一致");
        }
        //根据paymentNumber查ipay保单支付状态
        String messageId = "";
        String orderNumber = policy.getPaymentNumber();
        String channel = "iPay";

        Boolean flag = true;//mock ipay 返回
        //订单状态为未支付或者支付中时，成功返回orderNumber和redirectUrl
        SiebreCloudApplicationResult result = new SiebreCloudApplicationResult();
        if (flag) {
            result.setResultCode(200);
            result.setOrderNumber("test01");
            result.setGrossPremium(policy.getGrossPremium());
            result.setProductName(policy.getAgreementSpec().getName());
            result.setRedirectUrl("http://uat.mobile.siebre.com/siebre-cloud/pay");
        }else {
            result.setResultCode(400);
        }

        return result;
    }

    public SiebreCloudApplicationResult errorResult(String errorMessage) {
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
