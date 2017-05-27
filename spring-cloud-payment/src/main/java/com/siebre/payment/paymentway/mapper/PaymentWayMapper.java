package com.siebre.payment.paymentway.mapper;

import org.springframework.stereotype.Repository;

import com.siebre.payment.paymentway.entity.PaymentWay;

import java.util.List;

@Repository
public interface PaymentWayMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentWay record);

    int insertSelective(PaymentWay record);

    PaymentWay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentWay record);

    int updateByPrimaryKey(PaymentWay record);           
    
    PaymentWay  getPaymentWayByCode(String paymentWayCode);

    List<PaymentWay> getPaymentWayByChannelId(Long channelId);
}