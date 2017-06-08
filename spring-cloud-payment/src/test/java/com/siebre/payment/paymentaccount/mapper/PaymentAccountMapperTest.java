package com.siebre.payment.paymentaccount.mapper;

import com.siebre.base.DbTestConfig;
import com.siebre.payment.entity.enums.BankAccountType;
import com.siebre.payment.entity.enums.PaymentAccountType;
import com.siebre.payment.paymentaccount.entity.PaymentAccount;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by tianci.huang on 2017/6/7.
 */
public class PaymentAccountMapperTest extends DbTestConfig {

    @Autowired
    private PaymentAccountMapper paymentAccountMapper;

    @Test
    @Transactional
    public void deleteByPrimaryKey() throws Exception {
        PaymentAccount paymentAccount = getPaymentAccount();
        paymentAccountMapper.insert(paymentAccount);

        paymentAccountMapper.deleteByPrimaryKey(paymentAccount.getId());

        PaymentAccount p2 = paymentAccountMapper.selectByPrimaryKey(paymentAccount.getId());
        assertNull(p2);
    }

    private PaymentAccount getPaymentAccount() {
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setAccountType(BankAccountType.SAVING);
        paymentAccount.setAcountNumber("123456");
        paymentAccount.setBankCode("ICBC");
        paymentAccount.setHolderName("黄晓明");
        paymentAccount.setNickName("小明");
        paymentAccount.setOpenid("12345648946");
        paymentAccount.setType(PaymentAccountType.BANK_ACCOUNT);
        return paymentAccount;
    }

    @Test
    @Transactional
    public void insert() throws Exception {
        PaymentAccount paymentAccount = getPaymentAccount();
        paymentAccountMapper.insert(paymentAccount);
        PaymentAccount p2 = paymentAccountMapper.selectByPrimaryKey(paymentAccount.getId());
        assertAllFiledTrue(paymentAccount, p2);
    }

    private void assertAllFiledTrue(PaymentAccount paymentAccount, PaymentAccount p2) {
        assertTrue(p2.getAccountType().equals(paymentAccount.getAccountType()));
        assertTrue(p2.getAcountNumber().equals(paymentAccount.getAcountNumber()));
        assertTrue(p2.getBankCode().equals(paymentAccount.getBankCode()));
        assertTrue(p2.getHolderName().equals(paymentAccount.getHolderName()));
        assertTrue(p2.getNickName().equals(paymentAccount.getNickName()));
        assertTrue(p2.getOpenid().equals(paymentAccount.getOpenid()));
        assertTrue(p2.getType().equals(paymentAccount.getType()));
    }

    @Test
    @Transactional
    public void insertSelective() throws Exception {
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setAccountType(BankAccountType.SAVING);
        paymentAccount.setAcountNumber("123456");
        paymentAccount.setBankCode("ICBC");
        paymentAccount.setHolderName("黄晓明");
        paymentAccount.setNickName("小明");
        paymentAccountMapper.insertSelective(paymentAccount);
        PaymentAccount p2 = paymentAccountMapper.selectByPrimaryKey(paymentAccount.getId());
        assertTrue(p2.getAccountType().equals(paymentAccount.getAccountType()));
        assertTrue(p2.getAcountNumber().equals(paymentAccount.getAcountNumber()));
        assertTrue(p2.getBankCode().equals(paymentAccount.getBankCode()));
        assertTrue(p2.getHolderName().equals(paymentAccount.getHolderName()));
        assertTrue(p2.getNickName().equals(paymentAccount.getNickName()));
        assertNull(p2.getOpenid());
        assertNull(p2.getType());
    }

    @Test
    @Transactional
    public void selectByPrimaryKey() throws Exception {
        PaymentAccount paymentAccount = getPaymentAccount();
        paymentAccountMapper.insert(paymentAccount);
        PaymentAccount p2 = paymentAccountMapper.selectByPrimaryKey(paymentAccount.getId());
        assertAllFiledTrue(paymentAccount, p2);
    }

    @Test
    @Transactional
    public void updateByPrimaryKeySelective() throws Exception {
        PaymentAccount paymentAccount = getPaymentAccount();
        paymentAccountMapper.insert(paymentAccount);
        PaymentAccount p2 = new PaymentAccount();
        p2.setId(paymentAccount.getId());
        p2.setOpenid("HFNBSPF");
        paymentAccountMapper.updateByPrimaryKeySelective(p2);
        PaymentAccount p3 = paymentAccountMapper.selectByPrimaryKey(paymentAccount.getId());
        assertTrue(p3.getOpenid().equals(p2.getOpenid()));
        assertFalse(p3.getOpenid().equals(paymentAccount.getOpenid()));

        assertTrue(p3.getAccountType().equals(paymentAccount.getAccountType()));
        assertTrue(p3.getAcountNumber().equals(paymentAccount.getAcountNumber()));
        assertTrue(p3.getBankCode().equals(paymentAccount.getBankCode()));
        assertTrue(p3.getHolderName().equals(paymentAccount.getHolderName()));
        assertTrue(p3.getNickName().equals(paymentAccount.getNickName()));
        assertTrue(p3.getType().equals(paymentAccount.getType()));
    }

    @Test
    @Transactional
    public void updateByPrimaryKey() throws Exception {
        PaymentAccount paymentAccount = getPaymentAccount();
        paymentAccountMapper.insert(paymentAccount);
        PaymentAccount p2 = new PaymentAccount();
        p2.setId(paymentAccount.getId());
        p2.setType(PaymentAccountType.WECHAT_ACCOUNT);
        p2.setAccountType(BankAccountType.SAVING);
        p2.setAcountNumber("12345623234");
        p2.setBankCode("ICBC2");
        p2.setHolderName("黄晓明2");
        p2.setNickName("小明2");
        p2.setOpenid("adafad");
        paymentAccountMapper.updateByPrimaryKey(p2);
        PaymentAccount p3 = paymentAccountMapper.selectByPrimaryKey(paymentAccount.getId());
        assertAllFiledTrue(p2, p3);
    }

}