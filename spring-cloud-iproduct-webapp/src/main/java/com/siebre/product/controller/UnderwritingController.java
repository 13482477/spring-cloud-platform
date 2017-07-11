package com.siebre.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siebre.policy.application.Application;
import com.siebre.policy.application.Exception.SiebreCloudAgreementValidationError;
import com.siebre.policy.application.SiebreCloudApplicationResult;
import com.siebre.policy.application.service.SiebreCloudApplicationService;
import com.siebre.product.messagedemo.controller.messageobject.QuoteResult;
import com.siebre.product.utils.QuoteJsonService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meilan on 2017/7/5.
 */
@RestController
public class UnderwritingController {
    @Autowired
    private SiebreCloudApplicationService applicationService;

    @Autowired
    private QuoteJsonService quoteJsonService;

    private ObjectMapper jsonMapper = new ObjectMapper();

    @RequestMapping(path = "/api/v1/underwriting", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "预核保", notes = "预核保提交保单的json数据")
    public SiebreCloudApplicationResult underwriting(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteResult quoteResult = new QuoteResult();

        String requestJsonString = IOUtils.toString(request.getInputStream());
        Application application = null;
        try {
            application = quoteJsonService.toApplication(requestJsonString);
        } catch (Exception e) {
            //e.printStackTrace();
            List<SiebreCloudAgreementValidationError> errors = new ArrayList<SiebreCloudAgreementValidationError>();
            SiebreCloudAgreementValidationError error = new SiebreCloudAgreementValidationError(null,null, null);
            error.setDescription("requestData to application error");
            errors.add(error);
            SiebreCloudApplicationResult result = new SiebreCloudApplicationResult(null,errors);
            result.setResultCode(500);
            return result;
        }

        Map<String, Object> properties = jsonMapper.readValue(requestJsonString, HashMap.class);
        String applicationNumber = (String) properties.get("applicationNumber");

        SiebreCloudApplicationResult result = applicationService.underwriting(application, applicationNumber);
        result.setResultCode(response.getStatus());
        return result;
    }

}
