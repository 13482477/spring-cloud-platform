package com.siebre.payment.policylibility.mapper;

import org.springframework.stereotype.Repository;

import com.siebre.payment.policylibility.entity.PolicyLibility;

@Repository
public interface PolicyLibilityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PolicyLibility record);

    int insertSelective(PolicyLibility record);

    PolicyLibility selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PolicyLibility record);

    int updateByPrimaryKey(PolicyLibility record);
}