package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconJob;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconJobMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconJob record);

    int insertSelective(ReconJob record);

    ReconJob selectByPrimaryKey(Long id);

    ReconJob selectByJobName(String name);

    int updateByPrimaryKeySelective(ReconJob record);

    int updateByPrimaryKey(ReconJob record);
}