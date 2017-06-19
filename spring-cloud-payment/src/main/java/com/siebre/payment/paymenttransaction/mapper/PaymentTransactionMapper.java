package com.siebre.payment.paymenttransaction.mapper;

import com.siebre.basic.query.PageInfo;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentTransactionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentTransaction record);

    int insertSelective(PaymentTransaction record);

    PaymentTransaction selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentTransaction record);

    int updateByPrimaryKey(PaymentTransaction record);

    List<PaymentTransaction> queryPaymentTransaction(
            @Param("orderNumber") String orderNumber,
            @Param("applicantNumber") String applicantNumber,
            @Param("status") PaymentTransactionStatus status,
            @Param("interfaceType") PaymentInterfaceType type,
            @Param("channelId") Long channelId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("pageInfo") PageInfo pageInfo
    );

    PaymentTransaction selectByInterTradeNo(@Param("internalTn") String internalTn,
                                            @Param("transactionStatus") PaymentTransactionStatus transactionStatus,
                                            @Param("interfaceType") PaymentInterfaceType interfaceType);

    List<PaymentTransaction> selectByPaymentOrderId(Long id);

    int updateTransactionStatusToClose(String orderNumber);

    List<PaymentTransaction> selectTransaction(@Param("orderId") Long orderId, @Param("interfaceType") PaymentInterfaceType interfaceType);

}