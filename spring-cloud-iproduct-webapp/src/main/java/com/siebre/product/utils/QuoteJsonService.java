package com.siebre.product.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.siebre.agreement.dto.support.AgreementDtoBuilder;
import com.siebre.agreement.dto.support.DtoBuilders;
import com.siebre.agreement.dto.support.RoleDtoBuilder;
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
import java.util.List;
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

		AgreementDtoBuilder agreementDtoBuilder = DtoBuilders.agreementOf(productCode);
		for (String key : properties.keySet()) {
			if ("specCode".equals(key))
				continue;
			
			agreementDtoBuilder.property(key, properties.get(key));
		}

//		ArrayList<Object> roles = (ArrayList<Object>)properties.get("roles");
//		String insuredRole = (String)roles.get(0);
//		Map<String, Object> roleProperties = jsonMapper.readValue(insuredRole, HashMap.class);
//		RoleDtoBuilder roleDtoBuilder = DtoBuilders.roleOf(roleProperties.get("kind").toString());
//		for (String key : roleProperties.keySet()) {
//			roleDtoBuilder.property(key, roleProperties.get(key));
//		}

		RoleDtoBuilder roleDtoBuilder2 = DtoBuilders.roleOf("insured");
		Map<String, Object> insuredProperties = ((List<Map<String, Object>>) properties.get("roles")).parallelStream().filter(o ->
			"insured".equals(o.get("kind"))
		).findFirst().get();

		for (String key : insuredProperties.keySet()) {
			roleDtoBuilder2.property(key, insuredProperties.get(key));
		}

		agreementDtoBuilder.roles(roleDtoBuilder2);
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
