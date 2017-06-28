package com.siebre.policy.application.service;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.siebre.agreement.Agreement;
import com.siebre.agreement.AgreementRequest;
import com.siebre.agreement.AgreementRequestResult;
import com.siebre.agreement.AgreementSpec;
import com.siebre.agreement.factory.AgreementFactory;
import com.siebre.agreement.factory.DtoAgreementFactory;
import com.siebre.agreement.service.AgreementRequestExecutor;
import com.siebre.agreement.validation.InvalidAgreementException;
import com.siebre.policy.application.Application;
import com.siebre.policy.application.SiebreCloudApplicationResult;
import com.siebre.policy.factory.PolicyFactoryInterceptors;

import java.math.BigDecimal;

/**
 * Created by huangfei on 2017/06/27.
 */
public class SiebreCloudApplicationService {

    private AgreementRequestExecutor requestExecutor;

    private ProductRegistry productRegistry;

    private AgreementFactory agreementFactory;

    public SiebreCloudApplicationService(AgreementRequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
        productRegistry = new ProductRegistry();
        agreementFactory = new DtoAgreementFactory(productRegistry).withInterceptors(PolicyFactoryInterceptors
                .customRegistrar()
                .buildParentRelation().build());
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
            return new SiebreCloudApplicationResult(requestResult);
        } catch (InvalidAgreementException e) {
            return new SiebreCloudApplicationResult(policy, e.getErrors());
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
}
