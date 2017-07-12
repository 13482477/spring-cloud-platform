package com.siebre.policy.application.service;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.siebre.agreement.*;
import com.siebre.agreement.factory.AgreementFactory;
import com.siebre.agreement.factory.DtoAgreementFactory;
import com.siebre.agreement.service.AgreementRequestExecutor;
import com.siebre.policy.InsurancePolicy;
import com.siebre.policy.InsurancePolicyImpl;
import com.siebre.policy.application.Application;
import com.siebre.policy.application.Exception.SiebreCloudAgreementValidationError;
import com.siebre.policy.application.SiebreCloudApplicationResult;
import com.siebre.policy.dao.InsurancePolicyRepository;
import com.siebre.policy.factory.PolicyFactoryInterceptors;
import com.siebre.repository.rdb.hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.omg.PortableServer.POA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by huangfei on 2017/06/27.
 */
public class SiebreCloudApplicationService {

    private AgreementRequestExecutor requestExecutor;

    private ProductRegistry productRegistry;

    private AgreementFactory agreementFactory;

    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;

    @Autowired
    private SessionFactory sessionFactory;

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
        } catch (AgreementException e) {
            //e.printStackTrace();
            List<SiebreCloudAgreementValidationError> errors = new ArrayList<SiebreCloudAgreementValidationError>();
            SiebreCloudAgreementValidationError error = new SiebreCloudAgreementValidationError(null,null,null);
            error.setDescription(e.getCause().getCause().getMessage());
            errors.add(error);
            return new SiebreCloudApplicationResult(null, errors);
        }
    }

    public SiebreCloudApplicationResult underwriting(Application application, String applicationNumber) {
        AgreementSpec agreementSpec = application.getAgreementSpec();
        productRegistry.register(agreementSpec);
        Agreement agreement = agreementFactory.create(application.toDto());

        AgreementRequest request = agreement.createRequest("underwriting");

        try {
            AgreementRequestResult requestResult = requestExecutor.execute(request);

            if (requestResult.getErrorMessages().size() == 0){

                InsurancePolicy policy = (InsurancePolicy) agreement;
                policy.setApplicationNumber(applicationNumber);
                SiebreCloudApplicationResult result = new SiebreCloudApplicationResult(policy,null);

                //与保险公司核保接口mock

                //保存保单操作
                insurancePolicyRepository.save(policy);
                return result;
            }else {
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

                return new SiebreCloudApplicationResult(null,errors);
            }
            //return null;
        }catch (AgreementException e) {
            //e.printStackTrace();
            List<SiebreCloudAgreementValidationError> errors = new ArrayList<SiebreCloudAgreementValidationError>();
            SiebreCloudAgreementValidationError error = new SiebreCloudAgreementValidationError(null,e.getCause().getCause().getMessage(), null);
            error.setDescription(e.getCause().getCause().getMessage());
            errors.add(error);
            return new SiebreCloudApplicationResult(null,errors);
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
