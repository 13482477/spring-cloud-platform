package com.siebre.payment.paymentorder.mapper;

import com.siebre.payment.paymentorder.entity.PaymentOrderHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOrderHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentOrderHistory record);

    int insertSelective(PaymentOrderHistory record);

    PaymentOrderHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentOrderHistory record);

    int updateByPrimaryKeyWithBLOBs(PaymentOrderHistory record);

    int updateByPrimaryKey(PaymentOrderHistory record);
}