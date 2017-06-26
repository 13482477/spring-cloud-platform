package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconJobInstance;

public interface ReconJobInstanceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconJobInstance record);

    int insertSelective(ReconJobInstance record);

    ReconJobInstance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReconJobInstance record);

    int updateByPrimaryKey(ReconJobInstance record);
}