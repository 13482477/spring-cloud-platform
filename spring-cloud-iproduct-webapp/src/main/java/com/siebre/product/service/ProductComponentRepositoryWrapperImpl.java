package com.siebre.product.service;

import com.siebre.product.ProductComponent;
import com.siebre.product.dao.ProductComponentRepository;
import com.siebre.product.repository.InsuranceProductRepositoryImpl;
import com.siebre.repository.GeneralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by Admin on 2017/7/13.
 */
@Service("productComponentRepositoryWrapperImpl")
public class ProductComponentRepositoryWrapperImpl extends InsuranceProductRepositoryImpl implements
        ProductComponentRepositoryWrapper {

    private ProductComponentRepository productComponentRepository;

    private GeneralRepository generalRepository;

    @Autowired
    public ProductComponentRepositoryWrapperImpl(GeneralRepository generalRepository, ProductComponentRepository productComponentRepository) {
        super(generalRepository);
        this.generalRepository = generalRepository;
        this.productComponentRepository = productComponentRepository;
    }

    @Transactional
    @Override
    public void save(ProductComponent var1) {
        ProductComponent productComponent = generalRepository.findOne(ProductComponent.class, "currentVersion", true);
        productComponentRepository.save(var1);
    }
}
