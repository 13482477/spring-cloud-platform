package com.siebre.payment.paymentaccount.mapper;

import com.siebre.payment.paymentaccount.entity.PaymentAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentAccount record);

    int insertSelective(PaymentAccount record);

    PaymentAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentAccount record);

    int updateByPrimaryKey(PaymentAccount record);
}