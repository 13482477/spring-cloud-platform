package com.siebre.payment.paymentorderitem.mapper;

import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

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