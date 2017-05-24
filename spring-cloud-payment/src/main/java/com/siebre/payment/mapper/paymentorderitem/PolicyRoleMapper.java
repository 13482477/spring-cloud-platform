package com.siebre.payment.mapper.paymentorderitem;

import com.siebre.payment.entity.paymentorderitem.PolicyRole;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PolicyRole record);

    int insertSelective(PolicyRole record);

    PolicyRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PolicyRole record);

    int updateByPrimaryKey(PolicyRole record);
}