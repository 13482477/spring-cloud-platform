package com.siebre.payment.mapper.paymentinterface;

import org.springframework.stereotype.Repository;

import com.siebre.payment.entity.paymentinterface.PaymentInterface;

@Repository
public interface PaymentInterfaceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentInterface record);

    int insertSelective(PaymentInterface record);

    PaymentInterface selectByPrimaryKey(Long id);

    PaymentInterface selectByCode(String interfaceCode);

    int updateByPrimaryKeySelective(PaymentInterface record);

    int updateByPrimaryKey(PaymentInterface record);
}