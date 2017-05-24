package com.siebre.payment.mapper.transactionmessage;

import org.springframework.stereotype.Repository;

import com.siebre.payment.entity.transactionmessage.TransactionMessage;

@Repository
public interface TransactionMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TransactionMessage record);

    int insertSelective(TransactionMessage record);

    TransactionMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransactionMessage record);

    int updateByPrimaryKey(TransactionMessage record);
}