package com.siebre.payment.mapper.paymentway;

import org.springframework.stereotype.Repository;

import com.siebre.payment.entity.paymentway.PaymentWay;

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