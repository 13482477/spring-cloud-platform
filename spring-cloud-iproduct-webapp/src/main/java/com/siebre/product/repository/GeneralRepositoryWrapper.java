package com.siebre.product.repository;

import com.siebre.repository.GeneralRepository;

import java.util.Map;

/**
 * 重写{@link GeneralRepository}接口，添加update方法.
 * Created by Admin on 2017/7/13.
 */
public interface GeneralRepositoryWrapper extends GeneralRepository {

    void update(Class<?> clazz, String key, Object value);

    void update(Class<?> clazz, Map<String, Object> conditions);
}
