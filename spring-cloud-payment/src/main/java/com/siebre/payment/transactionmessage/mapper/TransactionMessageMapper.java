package com.siebre.payment.transactionmessage.mapper;

import com.siebre.payment.transactionmessage.entity.TransactionMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TransactionMessage record);

    int insertSelective(TransactionMessage record);

    TransactionMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransactionMessage record);

    int updateByPrimaryKey(TransactionMessage record);
}