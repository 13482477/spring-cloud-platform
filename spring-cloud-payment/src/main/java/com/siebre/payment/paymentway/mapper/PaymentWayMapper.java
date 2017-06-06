package com.siebre.payment.paymentway.mapper;

import com.siebre.basic.query.PageInfo;
import com.siebre.payment.paymentway.entity.PaymentWay;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentWayMapper {

    int deleteByChannelId(Long channelId);

    int deleteByPrimaryKey(Long id);

    int insert(PaymentWay record);

    int insertSelective(PaymentWay record);

    PaymentWay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentWay record);

    int updateByPrimaryKey(PaymentWay record);

    PaymentWay getPaymentWayByCode(String paymentWayCode);

    List<PaymentWay> getPaymentWayByChannelId(Long channelId);

    List<PaymentWay> selectAllByPage(@Param("pageInfo") PageInfo pageInfo);
}