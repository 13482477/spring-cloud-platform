package com.siebre.payment.hostconfig.mapper;

import com.siebre.payment.hostconfig.entity.PaymentHostConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHostConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentHostConfig record);

    int insertSelective(PaymentHostConfig record);

    PaymentHostConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentHostConfig record);

    int updateByPrimaryKey(PaymentHostConfig record);
}