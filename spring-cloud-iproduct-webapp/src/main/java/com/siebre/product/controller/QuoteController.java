package com.siebre.product.controller;

import static com.siebre.agreement.builder.SmfSpecBuilders.*;
import static com.siebre.product.builder.ProductBuilders.marketableProduct;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.agreement.service.DefaultAgreementRequestExecutor;
import com.siebre.basic.web.WebResult;
import com.siebre.policy.application.Application;
import com.siebre.policy.application.ApplicationResult;
import com.siebre.policy.application.service.ApplicationService;
import com.siebre.product.InsuranceProduct;
import com.siebre.product.dao.InsuranceProductDao;
import com.siebre.product.messagedemo.controller.messageobject.QuoteResult;
import com.siebre.smf.groovy.GroovyMetaClassEnhancer;

@RestController
public class QuoteController {

	@Autowired
	InsuranceProductDao insuranceProductDao;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Bean
	public ApplicationService applicationService() {
		return new ApplicationService(new DefaultAgreementRequestExecutor());
	}
	
	public InsuranceProduct mockProduct() {
		GroovyMetaClassEnhancer.getInstance().enhance();
		
		InsuranceProduct product = marketableProduct().code("test quote")
				.properties(
						propertySpec("premium").dataType(BigDecimal.class),
						propertySpec("insuredAmount").dataType(BigDecimal.class))
				.calculations(calculation("premiumCalc").body("premium=insuredAmount * 0.1"))
				.requests(requestSpec("new business").calculations("premiumCalc"))
				.build();
		
		return product;
	}
	
	@RequestMapping(path = "/api/v1/quote")
	@ResponseStatus
	public WebResult<QuoteResult> quote(HttpServletRequest request) {
		//TODO transfer json object to application
		
		QuoteResult quoteResult = new QuoteResult();
		
		String productCode = ServletRequestUtils.getStringParameter(request, "productCode", "");
		if (StringUtils.isEmpty(productCode)) {
			return WebResult.<QuoteResult>builder().returnCode("200").returnMessage("ProductCode is not valid.").data(quoteResult).build();
		}
		
		//InsuranceProduct product = insuranceProductDao.findByExternalReference(productCode);
		
		InsuranceProduct product = mockProduct();
		
		if (product == null)
			return WebResult.<QuoteResult>builder().returnCode("200").returnMessage("Can not find product by productCode: " + productCode).data(quoteResult).build();
		
		Long insuredAmount = ServletRequestUtils.getLongParameter(request, "insuredAmount", 0);
		
		ApplicationResult result = applicationService.quote(Application.Builder.appFor(product)
				.insuredAmount(BigDecimal.valueOf(insuredAmount))
				.build());
		
		quoteResult = QuoteResult.builder().status(result.isSucceeded() ? "Success" : "Failure").messages(result.getMessages()).insuredAmount(BigDecimal.valueOf(insuredAmount)).premium(result.getPremium()).build();
		
		return WebResult.<QuoteResult>builder().returnCode("200").returnMessage("The operation is done successfully.").data(quoteResult).build();
	
	}
	
}
