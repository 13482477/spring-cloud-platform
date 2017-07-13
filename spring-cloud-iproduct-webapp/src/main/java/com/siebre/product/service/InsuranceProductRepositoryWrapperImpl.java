package com.siebre.product.service;

import com.siebre.agreement.AgreementSpec;
import com.siebre.product.InsuranceProduct;
import com.siebre.product.ProductComponent;
import com.siebre.product.dao.ProductComponentRepository;
import com.siebre.product.repository.InsuranceProductRepositoryImpl;
import com.siebre.repository.GeneralRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 2017/7/13.
 */
@Service("productComponentRepositoryWrapperImpl")
public class InsuranceProductRepositoryWrapperImpl extends InsuranceProductRepositoryImpl implements
        InsuranceProductRepositoryWrapper {

    private final Logger logger = Logger.getLogger(InsuranceProductRepositoryImpl.class);

    private GeneralRepository generalRepository;

    @Autowired
    public InsuranceProductRepositoryWrapperImpl(GeneralRepository generalRepository) {
        super(generalRepository);
        this.generalRepository = generalRepository;
    }

    @Transactional
    @Override
    public void save(InsuranceProduct newProduct) {
        Map<String, Object> map = new HashMap<>();
        map.put("currentVersion", true);
        map.put("externalReference", newProduct.getExternalReference());
        Collection<InsuranceProduct> currentProducts = (Collection<InsuranceProduct>) generalRepository.findAll(InsuranceProduct.class,
                map);
        if (currentProducts != null && currentProducts.size() > 0) {
            currentProducts.forEach(currentProduct -> {
                if (currentProduct.getKind().equals("marketableProduct")) {
                    int version = currentProduct.getVersion();
                    int newVersion = version + 1;
                    logger.debug("当前产品版本：" + version);
                    newProduct.setVersion(newVersion);
                    Collection<AgreementSpec> newCoverageAgreementSpecs = newProduct.getChildComponentsOfKind("coverage");
                    setChildAgreementSpecVersion(newCoverageAgreementSpecs, newVersion);
                    Collection<AgreementSpec> newBaseAgreementSpecs = newProduct.getChildComponentsOfKind("base");
                    setChildAgreementSpecVersion(newBaseAgreementSpecs, newVersion);

                    currentProduct.setCurrentVersion(false);
                    Collection<AgreementSpec> agreementSpecs = currentProduct.getChildComponentsOfKind("coverage");
                    setChildAgreementSpecCurrentVersion(agreementSpecs);
                    Collection<AgreementSpec> baseAgreementSpecs = currentProduct.getChildComponentsOfKind("base");
                    setChildAgreementSpecCurrentVersion(baseAgreementSpecs);
                    logger.debug("新产品版本：" + newProduct.getVersion());
                    generalRepository.save(currentProduct);
                    generalRepository.save(newProduct);
                }
            });
        } else {
            newProduct.setVersion(1);
            logger.debug("新产品版本：" + newProduct.getVersion());
            generalRepository.save(newProduct);
        }
    }

    public void setChildAgreementSpecCurrentVersion(Collection<AgreementSpec> agreementSpecs) {
        if (agreementSpecs != null && !agreementSpecs.isEmpty()) {
            for (AgreementSpec agreementSpec : agreementSpecs) {
                if (agreementSpec != null) {
                    agreementSpec.setCurrentVersion(false);
                }
            }
        }
    }

    public void setChildAgreementSpecVersion(Collection<AgreementSpec> agreementSpecs, int newVersion) {
        if (agreementSpecs != null && !agreementSpecs.isEmpty()) {
            for (AgreementSpec agreementSpec : agreementSpecs) {
                if (agreementSpec != null) {
                    agreementSpec.setVersion(newVersion);
                }
            }
        }
    }
}
