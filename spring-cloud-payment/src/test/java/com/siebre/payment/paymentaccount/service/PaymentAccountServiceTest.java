package com.siebre.payment.paymentaccount.service;

import com.siebre.base.MockitoTestConfig;
import com.siebre.payment.entity.enums.BankAccountType;
import com.siebre.payment.entity.enums.PaymentAccountType;
import com.siebre.payment.paymentaccount.entity.BankAccount;
import com.siebre.payment.paymentaccount.entity.PaymentAccount;
import com.siebre.payment.paymentaccount.entity.WeChatAccount;
import com.siebre.payment.paymentaccount.mapper.PaymentAccountMapper;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Created by tianci.huang on 2017/6/7.
 */
public class PaymentAccountServiceTest extends MockitoTestConfig {

    @Mock
    private PaymentAccountMapper paymentAccountMapper;

    @Mock
    private PaymentOrderMapper paymentOrderMapper;

    @InjectMocks
    private PaymentAccountService paymentAccountService;

    @Before
    public void setUp() throws Exception {
        when(this.paymentAccountMapper.insert(new PaymentAccount())).thenReturn(1);
        when(this.paymentOrderMapper.updateByPrimaryKeySelective(new PaymentOrder())).thenReturn(1);
        when(this.paymentAccountMapper.selectByPrimaryKey(2L))
                .thenAnswer(new Answer<PaymentAccount>() {
                    public PaymentAccount answer(InvocationOnMock invocation) throws Throwable {
                        PaymentAccount paymentAccount = new PaymentAccount();
                        paymentAccount.setId(2L);
                        paymentAccount.setType(PaymentAccountType.WECHAT_ACCOUNT);
                        paymentAccount.setNickName("小明");
                        paymentAccount.setOpenid("HFSLAHUC3423");
                        return paymentAccount;
                    }
                });
    }

    @Test
    public void insertBankAccount() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountType(BankAccountType.SAVING);
        bankAccount.setAcountNumber("123456789");
        bankAccount.setBankCode("ICBC");
        bankAccount.setHolderName("黄晓明");

        PaymentAccount p2 = paymentAccountService.insertBankAccount(1L, bankAccount);
        assertTrue(PaymentAccountType.BANK_ACCOUNT.equals(p2.getType()));
    }

    @Test
    public void insertWeChatAccount() throws Exception {
        WeChatAccount weChatAccount = new WeChatAccount();
        weChatAccount.setOpenid("HJDSKDFS324");
        weChatAccount.setNickName("小明");

        PaymentAccount p2 = paymentAccountService.insertWeChatAccount(1L, weChatAccount);
        assertTrue(PaymentAccountType.WECHAT_ACCOUNT.equals(p2.getType()));
    }

    @Test
    public void getPaymentAccountById() throws Exception {
        PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountById(2L);
        assertTrue(paymentAccount.getId().equals(2L));
    }

}