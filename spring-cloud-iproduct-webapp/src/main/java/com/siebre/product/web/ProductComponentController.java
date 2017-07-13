package com.siebre.product.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siebre.agreement.AgreementSpec;
import com.siebre.agreement.io.AgreementSpecReader;
import com.siebre.agreement.io.AgreementSpecReaderFactory;
import com.siebre.agreement.io.support.XmlAgreementSpecReaderFactory;
import com.siebre.product.InsuranceProduct;
import com.siebre.product.ProductComponent;
import com.siebre.product.dao.ProductComponentRepository;
import com.siebre.product.repository.InsuranceProductRepository;
import com.siebre.product.service.SiebreCloudProductManager;
import com.siebre.smf.SmfProperty;
import com.siebre.smf.SmfRole;
import com.siebre.smf.web.SmfBehaviorResource;
import org.apache.commons.lang3.StringUtils;
import org.jdto.DTOBinder;
import org.jdto.DTOBinderFactory;
import org.jdto.annotation.DTOCascade;
import org.jdto.annotation.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
final class ProductComponentController {

	@Autowired
	private InsuranceProductRepository productRepository;

	@Autowired
	private ProductComponentRepository productComponentRepository;

	@Autowired
	private ObjectMapper jsonMapper;

	@Autowired
	private SiebreCloudProductManager productManager;

	private DTOBinder binder = DTOBinderFactory.getBinder();


	private static String CONTENT_TYPE = "application/json; charset=utf-8";

	@RequestMapping(value = "/marketableProduct", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public void queryMarketableProduct(HttpServletResponse response) throws IOException {
		Collection<InsuranceProduct> products = productRepository.findAll();

		response.setContentType(CONTENT_TYPE);
		jsonMapper.writeValue(response.getWriter(), binder.bindFromBusinessObjectCollection(ProductResource.class, products));
	}

	@RequestMapping(value = "/productComponent/{idOrCode}", method = RequestMethod.GET)
	public void retrieveMarketableProduct(@PathVariable String idOrCode, HttpServletResponse response) throws IOException {
		ProductComponent component = null;
		if (StringUtils.isNumeric(idOrCode)) {
			component = productComponentRepository.findOne(Long.valueOf(idOrCode));
		}
		if (component == null) {
			component = productComponentRepository.findByExternalReference(idOrCode);
		}

		response.setContentType(CONTENT_TYPE);

		ProductDetailResource resource = binder.bindFromBusinessObject(ProductDetailResource.class, component);
		addLinks(resource, "/agreementSpec/" + component.getOid() + '/');

		jsonMapper.writeValue(response.getWriter(), resource);
	}

	private void addLinks(ProductDetailResource resource, String baseUrl) {
		for (SmfBehaviorResource ruleResource : resource.getRules()) {
			ruleResource.setUrl(baseUrl + ruleResource.getInternalReference());
		}
		for (SmfBehaviorResource calculationResource : resource.getCalculations()) {
			calculationResource.setUrl(baseUrl + calculationResource.getInternalReference());
		}
		//TODO need a way to generate url from path
		for (ProductDetailResource componentResource : resource.getComponentSpecs()) {
			addLinks(componentResource, baseUrl + componentResource.getCode() + '.');
		}
	}

	/**
	 * 上传保单文件 Jinxin Xie
	 *
	 * @param policy 保单文件（xml）
	 */
	@RequestMapping(value = "/importMarketableProduct", method = RequestMethod.POST,  produces = "application/json; " +
			"charset=utf-8")
	public void importMarketableProduct(@RequestParam("policy") MultipartFile policy) {
		if (policy == null || policy.isEmpty()) {
			return;
		}
		try {
			AgreementSpecReaderFactory factory = new XmlAgreementSpecReaderFactory();
			AgreementSpecReader reader = factory.createReader(policy.getInputStream());
			if (reader != null) {
				AgreementSpec agreementSpec = reader.read();
				if (agreementSpec instanceof InsuranceProduct) {
//                    productRepository.save((InsuranceProduct) agreementSpec);
//                    insuranceProductDao.save((InsuranceProduct) agreementSpec);
					productManager.save((InsuranceProduct) agreementSpec);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/productComponent", method = RequestMethod.GET)
	public void queryProductComponent(HttpServletResponse response) throws IOException {
		Collection<ProductComponent> components = productComponentRepository.findNonmarketableComponents();

		response.setContentType(CONTENT_TYPE);
		jsonMapper.writeValue(response.getWriter(), binder.bindFromBusinessObjectCollection(ProductDetailResource.class, components));
	}


	public static class ProductDetailResource extends ProductResource {

		private Collection<SmfProperty> propertySpecs;

		private Collection<SmfRole> roleSpecs;

		@DTOCascade
		private List<SmfBehaviorResource> rules;

		@DTOCascade
		private List<SmfBehaviorResource> calculations;

		@DTOCascade
		@Source("childComponents")
		private List<ProductDetailResource> componentSpecs;

		public Collection<SmfProperty> getPropertySpecs() {
			return propertySpecs;
		}

		public Collection<SmfRole> getRoleSpecs() {
			return roleSpecs;
		}

		public List<ProductDetailResource> getComponentSpecs() {
			return componentSpecs;
		}

		public void setPropertySpecs(Collection<SmfProperty> propertySpecs) {
			this.propertySpecs = propertySpecs;
		}

		public void setRoleSpecs(Collection<SmfRole> roleSpecs) {
			this.roleSpecs = roleSpecs;
		}

		public List<SmfBehaviorResource> getRules() {
			return rules;
		}

		public void setRules(List<SmfBehaviorResource> rules) {
			this.rules = rules;
		}

		public List<SmfBehaviorResource> getCalculations() {
			return calculations;
		}

		public void setCalculations(List<SmfBehaviorResource> calculations) {
			this.calculations = calculations;
		}

		public void setComponentSpecs(List<ProductDetailResource> componentSpecs) {
			this.componentSpecs = componentSpecs;
		}
	}
}
