package com.siebre.payment.utils.txserial.mapper;

import com.siebre.payment.utils.txserial.model.TxSerialNumber;

public interface TxSerialNumberMapper {
    int deleteByPrimaryKey(String serialName);

    int insert(TxSerialNumber record);

    int insertSelective(TxSerialNumber record);

    TxSerialNumber selectByPrimaryKey(String serialName);

    int updateByPrimaryKeySelective(TxSerialNumber record);

    int updateByPrimaryKey(TxSerialNumber record);
}