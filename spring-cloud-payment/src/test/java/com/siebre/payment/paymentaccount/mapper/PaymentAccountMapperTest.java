package com.siebre.payment.paymentaccount.mapper;

import com.siebre.base.DbTestConfig;
import com.siebre.payment.entity.enums.BankAccountType;
import com.siebre.payment.paymentaccount.entity.PaymentAccount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by tianci.huang on 2017/6/7.
 */
public class PaymentAccountMapperTest extends DbTestConfig {

    @Autowired
    private PaymentAccountMapper paymentAccountMapper;

    @Test
    public void deleteByPrimaryKey() throws Exception {

    }

    @Test
    public void insert() throws Exception {
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setAccountType(BankAccountType.SAVING);
        paymentAccount.setAcountNumber("123456");
        paymentAccount.setBankCode("ICBC");
        paymentAccount.setHolderName("黄晓明");
        paymentAccount.setNickName("小明");
        paymentAccount.setOpenid("12345648946");

    }

    @Test
    public void insertSelective() throws Exception {

    }

    @Test
    public void selectByPrimaryKey() throws Exception {

    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {

    }

    @Test
    public void updateByPrimaryKey() throws Exception {

    }

}