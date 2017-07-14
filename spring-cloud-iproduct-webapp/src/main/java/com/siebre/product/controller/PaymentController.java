package com.siebre.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siebre.agreement.service.DefaultAgreementRequestExecutor;
import com.siebre.policy.application.SiebreCloudApplicationResult;
import com.siebre.policy.payment.service.SiebreCloudPaymentService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by meilan on 2017/7/12.
 */
@RestController
public class PaymentController {

    @Autowired
    private SiebreCloudPaymentService paymentService;

    private ObjectMapper jsonMapper = new ObjectMapper();

    @RequestMapping(path = "/api/v1/iproductPayment", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "发起支付", notes = "发起支付提交的json数据")
    public SiebreCloudApplicationResult prePay(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String requestJsonString = IOUtils.toString(request.getInputStream());
        Map<String, Object> properties = jsonMapper.readValue(requestJsonString, HashMap.class);

        SiebreCloudApplicationResult result = paymentService.prePay(properties);
        return result;
    }

}
