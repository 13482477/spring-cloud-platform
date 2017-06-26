package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconJobInstance;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconJobInstanceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconJobInstance record);

    int insertSelective(ReconJobInstance record);

    ReconJobInstance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReconJobInstance record);

    int updateByPrimaryKey(ReconJobInstance record);
}