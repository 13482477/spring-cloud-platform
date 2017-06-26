package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconItem;

public interface ReconItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconItem record);

    int insertSelective(ReconItem record);

    ReconItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReconItem record);

    int updateByPrimaryKey(ReconItem record);
}