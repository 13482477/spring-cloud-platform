package com.siebre.product.repository;

import com.siebre.repository.rdb.hibernate.HibernateGeneralRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by Admin on 2017/7/13.
 */
@Repository("generalRepositoryWrapperImpl")
public class GeneralRepositoryWrapperImpl extends HibernateGeneralRepository implements GeneralRepositoryWrapper {

    @Transactional
    @Override
    public void update(Class<?> clazz, String key, Object value) {

    }

    @Transactional
    @Override
    public void update(Class<?> clazz, Map<String, Object> conditions) {

    }
}
