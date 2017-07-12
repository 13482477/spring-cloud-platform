package com.siebre.policy.application.service;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.siebre.agreement.*;
import com.siebre.agreement.factory.AgreementFactory;
import com.siebre.agreement.factory.DtoAgreementFactory;
import com.siebre.agreement.service.AgreementRequestExecutor;
import com.siebre.policy.application.Application;
import com.siebre.policy.application.Exception.SiebreCloudAgreementValidationError;
import com.siebre.policy.application.SiebreCloudApplicationResult;
import com.siebre.policy.factory.PolicyFactoryInterceptors;
import com.siebre.redis.sequence.SequenceGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangfei on 2017/06/27.
 */

public class SiebreCloudApplicationService {

    private AgreementRequestExecutor requestExecutor;

    private ProductRegistry productRegistry;

    private AgreementFactory agreementFactory;

    public SequenceGenerator getApplicationNumberGenerator() {
        return applicationNumberGenerator;
    }

    public void setApplicationNumberGenerator(SequenceGenerator applicationNumberGenerator) {
        this.applicationNumberGenerator = applicationNumberGenerator;
    }

    private SequenceGenerator applicationNumberGenerator;

    public SiebreCloudApplicationService(AgreementRequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
        productRegistry = new ProductRegistry();
        agreementFactory = new DtoAgreementFactory(productRegistry).withInterceptors(PolicyFactoryInterceptors
                .customRegistrar()
                .buildParentRelation().build());
    }

    public SiebreCloudApplicationService(AgreementRequestExecutor requestExecutor, SequenceGenerator applicationNumberGenerator) {
        this.requestExecutor = requestExecutor;
        productRegistry = new ProductRegistry();
        agreementFactory = new DtoAgreementFactory(productRegistry).withInterceptors(PolicyFactoryInterceptors
                .customRegistrar()
                .buildParentRelation().build());

        this.applicationNumberGenerator = applicationNumberGenerator;
    }

    public SiebreCloudApplicationService(AgreementFactory agreementFactory, AgreementRequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
        productRegistry = new ProductRegistry();
        this.agreementFactory = agreementFactory;
    }

    public SiebreCloudApplicationResult quote(Application application) {

        AgreementSpec agreementSpec = application.getAgreementSpec();
        productRegistry.register(agreementSpec);
        Agreement policy = agreementFactory.create(application.toDto());

        AgreementRequest request = policy.createRequest("quote");
//		if (product.hasRequestSpec("quote")) {
//			request = policy.createRequest("quote");
//		} else {
//			request = requestSpec("quote").calculations("premiumCalc").build().createActual(policy);
//		}
        try {
            AgreementRequestResult requestResult = requestExecutor.execute(request);
            requestResult.getAgreement().setSmfProperty("applicationNumber", applicationNumberGenerator.next());
            return new SiebreCloudApplicationResult(requestResult);
        } catch (AgreementException e) {
            //e.printStackTrace();
            List<SiebreCloudAgreementValidationError> errors = new ArrayList<SiebreCloudAgreementValidationError>();
            SiebreCloudAgreementValidationError error = new SiebreCloudAgreementValidationError(null,null,null);
            error.setDescription(e.getCause().getCause().getMessage());
            errors.add(error);
            return new SiebreCloudApplicationResult(policy, errors);
        }
    }

    public SiebreCloudApplicationResult underwriting(Application application) {
        AgreementSpec agreementSpec = application.getAgreementSpec();
        productRegistry.register(agreementSpec);
        Agreement policy = agreementFactory.create(application.toDto());

        //AgreementRole applicantRole = policy.createRole("Applicant");
        AgreementRequest request = policy.createRequest("underwriting");

        try {
            AgreementRequestResult requestResult = requestExecutor.execute(request);
            SiebreCloudApplicationResult result = new SiebreCloudApplicationResult(policy,null);
            if (requestResult.getErrorMessages().size() == 0){
                result.setUnderwritingResult(true);

                //与保险公司核保接口mock

            } else {
                result.setUnderwritingResult(false);
                Set<String> flag = new HashSet<>();
                List<SiebreCloudAgreementValidationError> errors = new ArrayList<SiebreCloudAgreementValidationError>();
                for(String errorMessage: requestResult.getErrorMessages()){
                    if(flag.contains(errorMessage)) {
                        continue;
                    }

                    flag.add(errorMessage);
                    SiebreCloudAgreementValidationError error = new SiebreCloudAgreementValidationError(null,null,null);
                    error.setDescription(errorMessage);
                    errors.add(error);
                }
                result.setErrors(errors);
            }
            return result;
        } catch (AgreementException e) {
            e.printStackTrace();
            List<SiebreCloudAgreementValidationError> errors = new ArrayList<SiebreCloudAgreementValidationError>();
            SiebreCloudAgreementValidationError error = new SiebreCloudAgreementValidationError(null,e.getCause().getCause().getMessage(), null);
            error.setDescription(e.getCause().getCause().getMessage());
            errors.add(error);
            return new SiebreCloudApplicationResult(policy, errors);
        }
    }
      /**
     * A temporary product registry to serve products to AgreementFactory.
     *
     * @author ZhangChi
     * @since 2015年1月5日
     */
    static class ProductRegistry implements AgreementFactory.AgreementSpecLocator {

        private Cache<String, AgreementSpec> cache = CacheBuilder.newBuilder().maximumSize(20).build();

        public AgreementSpec get(String code) {
            return cache.getIfPresent(code);
        }

        public void register(AgreementSpec agreementSpec) {
            Preconditions.checkNotNull(agreementSpec.getExternalReference(), "externalReference of supplied AgreementSpec is null");
            cache.put(agreementSpec.getExternalReference(), agreementSpec);
        }
    }

    public AgreementRequestExecutor getRequestExecutor() {
        return requestExecutor;
    }

    public void setRequestExecutor(AgreementRequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public ProductRegistry getProductRegistry() {
        return productRegistry;
    }

    public void setProductRegistry(ProductRegistry productRegistry) {
        this.productRegistry = productRegistry;
    }

    public AgreementFactory getAgreementFactory() {
        return agreementFactory;
    }

    public void setAgreementFactory(AgreementFactory agreementFactory) {
        this.agreementFactory = agreementFactory;
    }
}
