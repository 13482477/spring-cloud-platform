package com.siebre.product.utils;


import static com.siebre.agreement.builder.SmfSpecBuilders.calculation;
import static com.siebre.agreement.builder.SmfSpecBuilders.propertySpec;
import static com.siebre.agreement.builder.SmfSpecBuilders.requestSpec;
import static com.siebre.product.builder.ProductBuilders.marketableProduct;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siebre.agreement.dto.support.AgreementDtoBuilder;
import com.siebre.agreement.dto.support.DtoBuilders;
import com.siebre.policy.application.Application;
import com.siebre.product.InsuranceProduct;
import com.siebre.product.repository.InsuranceProductRepository;
import com.siebre.smf.groovy.GroovyMetaClassEnhancer;

@Service
@Transactional
public class QuoteJsonService {
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public QuoteJsonService() {
		jsonMapper.setDateFormat(dateFormat);
	}

	@Autowired
	InsuranceProductRepository productRepository;
	
	public Application toApplication(String requestJsonString) throws Exception {
		Map<String, Object> properties = jsonMapper.readValue(requestJsonString, HashMap.class);
		String productCode = (String) properties.get("specCode");
		
		InsuranceProduct product = productRepository.findByExternalReference(productCode);
		product.getRoleSpecs();
		product.getRequestSpecs();
		product.getCalculations();
		product.getRules();
		product.getPropertySpecs();
		product.getChildCompositions();
		
		//InsuranceProduct product = mockProduct();
		
		AgreementDtoBuilder agreementDtoBuilder = DtoBuilders.agreementOf(productCode);;
		for (String key : properties.keySet()) {
			if ("specCode".equals(key))
				continue;
			
			agreementDtoBuilder.property(key, properties.get(key));
		}
		
		
		Application app = new Application(product, agreementDtoBuilder.build());
		
		return app;
	}
	
	Map<String, Object> toMap(String requestJsonString) throws Exception {
		return jsonMapper.readValue(requestJsonString, HashMap.class);
	}
	
	
	public InsuranceProduct mockProduct() {
		GroovyMetaClassEnhancer.getInstance().enhance();
		
		InsuranceProduct product = marketableProduct().code("test")
				.properties(
						propertySpec("premium").dataType(BigDecimal.class),
						propertySpec("insuredAmount").dataType(BigDecimal.class))
				.calculations(calculation("premiumCalc").body("premium=insuredAmount * 0.1"))
				.requests(requestSpec("new business").calculations("premiumCalc"))
				.build();
		
		return product;
	}
	
	
}
