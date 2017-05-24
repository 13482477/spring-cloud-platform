package com.siebre.payment.mapper.paymentorderitem;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.siebre.payment.entity.paymentorderitem.PaymentOrderItem;

@Repository
public interface PaymentOrderItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentOrderItem record);

    int insertSelective(PaymentOrderItem record);

    PaymentOrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentOrderItem record);

    int updateByPrimaryKey(PaymentOrderItem record);
    
    List<PaymentOrderItem> selectByPaymentOrderId(Long id);
}