package com.siebre.payment.policyrole.mapper;

import org.springframework.stereotype.Repository;

import com.siebre.payment.policyrole.entity.PolicyRole;

@Repository
public interface PolicyRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PolicyRole record);

    int insertSelective(PolicyRole record);

    PolicyRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PolicyRole record);

    int updateByPrimaryKey(PolicyRole record);
}