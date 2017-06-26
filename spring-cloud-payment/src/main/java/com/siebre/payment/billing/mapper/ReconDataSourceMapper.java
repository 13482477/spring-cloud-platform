package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconDataSource;

public interface ReconDataSourceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconDataSource record);

    int insertSelective(ReconDataSource record);

    ReconDataSource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReconDataSource record);

    int updateByPrimaryKey(ReconDataSource record);
}