package com.siebre.product.controller;

import com.siebre.agreement.service.DefaultAgreementRequestExecutor;
import com.siebre.policy.application.Application;
import com.siebre.policy.application.SiebreCloudApplicationResult;
import com.siebre.policy.application.service.SiebreCloudApplicationService;
import com.siebre.product.messagedemo.controller.messageobject.QuoteResult;
import com.siebre.product.utils.QuoteJsonService;
import com.siebre.redis.sequence.SequenceGenerator;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class QuoteController {

	@Autowired
	private SiebreCloudApplicationService applicationService;
	
	@Autowired
	private QuoteJsonService quoteJsonService;

	@Autowired
	@Qualifier("applicationNumberGenerator")
	private SequenceGenerator applicationNumberGenerator;

	@Bean
	public SiebreCloudApplicationService applicationService(@Autowired SequenceGenerator applicationNumberGenerator) {
		return new SiebreCloudApplicationService(new DefaultAgreementRequestExecutor(), applicationNumberGenerator);
	}
	
	@RequestMapping(path = "/api/v1/quote", method = RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.OK)
	@ApiOperation(value = "试算", notes = "提交保单的json数据")
	public SiebreCloudApplicationResult quote(HttpServletRequest request, HttpServletResponse response) throws Exception {
		QuoteResult quoteResult = new QuoteResult();
		
		String requestJsonString = IOUtils.toString(request.getInputStream());
		Application application = quoteJsonService.toApplication(requestJsonString);

		return applicationService.quote(application);
	}

}
