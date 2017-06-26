package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconMatchRule;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconMatchRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconMatchRule record);

    int insertSelective(ReconMatchRule record);

    ReconMatchRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReconMatchRule record);

    int updateByPrimaryKey(ReconMatchRule record);
}