package com.siebre.product.service;

import com.siebre.product.ProductComponent;
import com.siebre.product.repository.InsuranceProductRepository;

/**
 * Created by Admin on 2017/7/13.
 */
public interface ProductComponentRepositoryWrapper extends InsuranceProductRepository {

    void save(ProductComponent var1);
}
