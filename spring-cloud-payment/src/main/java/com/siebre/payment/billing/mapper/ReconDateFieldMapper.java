package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconDateField;

public interface ReconDateFieldMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconDateField record);

    int insertSelective(ReconDateField record);

    ReconDateField selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReconDateField record);

    int updateByPrimaryKey(ReconDateField record);
}