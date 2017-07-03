package com.siebre.product.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.siebre.agreement.dto.AgreementDto;
import com.siebre.agreement.dto.ComponentData;
import com.siebre.agreement.dto.annotation.support.AnnotatedDto;
import com.siebre.policy.application.Application;
import com.siebre.product.InsuranceProduct;
import com.siebre.product.repository.InsuranceProductRepository;
import com.siebre.smf.SmfRole;
import com.siebre.smf.groovy.GroovyMetaClassEnhancer;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.siebre.agreement.builder.SmfSpecBuilders.*;
import static com.siebre.product.builder.ProductBuilders.marketableProduct;

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
		
		Hibernate.initialize(product.getRoleSpecs());
		for (SmfRole role : product.getRoleSpecs()) {
			Hibernate.initialize(role.getRules());
			Hibernate.initialize(role.getPropertySpecs());
			Hibernate.initialize(role.getCalculations());
			Hibernate.initialize(role.getConstantProperties());
		}
		Hibernate.initialize(product.getRequestSpecs());
		Hibernate.initialize(product.getCalculations());
		Hibernate.initialize(product.getRules());
		Hibernate.initialize(product.getPropertySpecs());
		Hibernate.initialize(product.getChildCompositions());
		Hibernate.initialize(product.getConstantProperties());
		
		//InsuranceProduct product = mockProduct();

		ObjectMapper mapper = new ObjectMapper();
		ComponentData<String, Object> componentData = mapper.readValue(requestJsonString, ComponentData.class);
		AgreementDto agreementDto = AnnotatedDto.wrapAs(componentData, AgreementDto.class);

		Application app = new Application(product, agreementDto);

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
