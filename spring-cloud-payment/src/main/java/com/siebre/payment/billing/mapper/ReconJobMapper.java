package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconJob;

public interface ReconJobMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconJob record);

    int insertSelective(ReconJob record);

    ReconJob selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReconJob record);

    int updateByPrimaryKey(ReconJob record);
}