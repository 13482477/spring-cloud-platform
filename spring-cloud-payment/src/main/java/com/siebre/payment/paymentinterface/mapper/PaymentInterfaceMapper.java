package com.siebre.payment.paymentinterface.mapper;

import org.springframework.stereotype.Repository;

import com.siebre.payment.paymentinterface.entity.PaymentInterface;

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