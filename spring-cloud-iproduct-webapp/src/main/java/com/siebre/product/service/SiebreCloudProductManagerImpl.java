package com.siebre.product.service;

import com.siebre.product.InsuranceProduct;
import com.siebre.repository.rdb.hibernate.HibernateUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Admin on 2017/7/12.
 */
@Service
public class SiebreCloudProductManagerImpl implements SiebreCloudProductManager {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void save(InsuranceProduct product) {
        HibernateUtils.getCurrentSession(sessionFactory).saveOrUpdate(product);
    }
}
