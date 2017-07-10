package com.siebre.product.controller;

import com.siebre.policy.application.Application;
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

/**
 * Created by meilan on 2017/7/5.
 */
@RestController
public class UnderwritingController {
    @Autowired
    private SiebreCloudApplicationService applicationService;

    @Autowired
    private QuoteJsonService quoteJsonService;

    @RequestMapping(path = "/api/v1/underwriting", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "预核保", notes = "预核保提交保单的json数据")
    public SiebreCloudApplicationResult underwriting(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteResult quoteResult = new QuoteResult();

        String requestJsonString = IOUtils.toString(request.getInputStream());
        Application application = quoteJsonService.toApplication(requestJsonString);

        return applicationService.underwriting(application);
    }

}
