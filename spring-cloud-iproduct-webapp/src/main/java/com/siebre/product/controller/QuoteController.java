package com.siebre.product.controller;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.agreement.service.DefaultAgreementRequestExecutor;
import com.siebre.basic.web.WebResult;
import com.siebre.policy.application.Application;
import com.siebre.policy.application.ApplicationResult;
import com.siebre.policy.application.service.ApplicationService;
import com.siebre.product.messagedemo.controller.messageobject.QuoteResult;
import com.siebre.product.utils.QuoteJsonService;

@RestController
public class QuoteController {

	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private QuoteJsonService quoteJsonService;
	
	@Bean
	public ApplicationService applicationService() {
		return new ApplicationService(new DefaultAgreementRequestExecutor());
	}
	
	@RequestMapping(path = "/api/v1/quote", method = RequestMethod.POST)
	@ResponseStatus
	@ApiOperation(value = "试算", notes = "提交保单的json数据")
	public WebResult<QuoteResult> quote(HttpServletRequest request) throws Exception {
		QuoteResult quoteResult = new QuoteResult();
		
		String requestJsonString = IOUtils.toString(request.getInputStream());
		Application application = quoteJsonService.toApplication(requestJsonString);
		
		ApplicationResult result = applicationService.quote(application);
		
		quoteResult = new QuoteResult.QuoteResultBuilder().status(result.isSucceeded() ? "Success" : "Failure").messages(result.getMessages()).premium(result.getPremium()).build();
		
		return WebResult.<QuoteResult>builder().returnCode("200").returnMessage("The operation is done successfully.").data(quoteResult).build();
	
	}
	
}
