package com.siebre.payment.hostconfig.mapper;

import com.siebre.payment.hostconfig.entity.PaymentHostConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentHostConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentHostConfig record);

    int insertSelective(PaymentHostConfig record);

    PaymentHostConfig selectByPrimaryKey(Long id);

    List<PaymentHostConfig> select();

    int updateByPrimaryKeySelective(PaymentHostConfig record);

    int updateByPrimaryKey(PaymentHostConfig record);
}