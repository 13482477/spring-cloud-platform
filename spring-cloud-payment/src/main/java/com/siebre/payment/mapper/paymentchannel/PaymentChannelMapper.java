package com.siebre.payment.mapper.paymentchannel;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.siebre.basic.query.PageInfo;
import com.siebre.payment.entity.paymentchannel.PaymentChannel;

@Repository
public interface PaymentChannelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentChannel record);

    int insertSelective(PaymentChannel record);

    PaymentChannel selectByPrimaryKey(Long id);
    
    PaymentChannel  selectByChannelCode(String channelCode);

    int updateByPrimaryKeySelective(PaymentChannel record);

    int updateByPrimaryKey(PaymentChannel record);
    
    List<PaymentChannel> selectAll();
    
    List<PaymentChannel> selectAllByPage(@Param("pageInfo") PageInfo pageInfo);
    
}