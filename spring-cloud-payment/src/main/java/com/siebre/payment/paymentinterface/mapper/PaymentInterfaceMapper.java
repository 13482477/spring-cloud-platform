package com.siebre.payment.paymentinterface.mapper;

import com.siebre.basic.query.PageInfo;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentInterfaceMapper {

    int deleteByPaymentWayId(Long paymentWayId);

    int deleteByPrimaryKey(Long id);

    int insert(PaymentInterface record);

    int insertSelective(PaymentInterface record);

    PaymentInterface selectByPrimaryKey(Long id);

    PaymentInterface selectByCode(String interfaceCode);

    int updateByPrimaryKeySelective(PaymentInterface record);

    int updateByPrimaryKey(PaymentInterface record);

    List<PaymentInterface> selectByPaymentWayId(Long paymentWayId);

    List<PaymentInterface> selectByPage(@Param("pageInfo") PageInfo pageInfo);
}