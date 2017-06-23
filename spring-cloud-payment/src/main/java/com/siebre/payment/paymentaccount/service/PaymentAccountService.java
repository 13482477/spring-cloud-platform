package com.siebre.payment.paymentaccount.service;

import com.siebre.payment.entity.enums.PaymentAccountType;
import com.siebre.payment.paymentaccount.entity.AliPayAccount;
import com.siebre.payment.paymentaccount.entity.BankAccount;
import com.siebre.payment.paymentaccount.entity.PaymentAccount;
import com.siebre.payment.paymentaccount.entity.WeChatAccount;
import com.siebre.payment.paymentaccount.mapper.PaymentAccountMapper;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Huang Tianci
 */
@Service("paymentAccountService")
public class PaymentAccountService {

    @Autowired
    private PaymentAccountMapper paymentAccountMapper;

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Transactional("db")
    public PaymentAccount insertBankAccount(Long orderId, BankAccount bankAccount) {
        PaymentAccount paymentAccount = transferToPaymentAccount(bankAccount);
        return insertPaymentAccount(orderId, paymentAccount);
    }

    @Transactional("db")
    public PaymentAccount insertWeChatAccount(Long orderId, WeChatAccount weChatAccount) {
        PaymentAccount paymentAccount = transferToPaymentAccount(weChatAccount);
        return insertPaymentAccount(orderId, paymentAccount);
    }

    @Transactional("db")
    public PaymentAccount insertAliPayAccount(Long orderId, AliPayAccount aliPayAccount) {
        PaymentAccount paymentAccount = transferToPaymentAccount(aliPayAccount);
        return insertPaymentAccount(orderId, paymentAccount);
    }

    @Transactional("db")
    public void updateBankAccount(BankAccount bankAccount) {
        PaymentAccount paymentAccount = transferToPaymentAccount(bankAccount);
        paymentAccountMapper.updateByPrimaryKeySelective(paymentAccount);
    }

    @Transactional("db")
    public void updateWeChatAccount(WeChatAccount weChatAccount){
        PaymentAccount paymentAccount = transferToPaymentAccount(weChatAccount);
        paymentAccountMapper.updateByPrimaryKeySelective(paymentAccount);
    }

    @Transactional("db")
    public void updateAliPayAccount(AliPayAccount aliPayAccount){
        PaymentAccount paymentAccount = transferToPaymentAccount(aliPayAccount);
        paymentAccountMapper.updateByPrimaryKeySelective(paymentAccount);
    }

    public PaymentAccount getPaymentAccountById(Long accountId){
        return paymentAccountMapper.selectByPrimaryKey(accountId);
    }

    @Transactional("db")
    public PaymentAccount insertPaymentAccount(Long orderId, PaymentAccount paymentAccount) {
        paymentAccountMapper.insert(paymentAccount);
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setId(orderId);
        paymentOrder.setPaymentAccountId(paymentAccount.getId());
        paymentOrderMapper.updateByPrimaryKeySelective(paymentOrder);
        return paymentAccount;
    }

    public PaymentAccount transferToPaymentAccount(AliPayAccount aliPayAccount) {
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setType(PaymentAccountType.ALIPAY_ACCOUNT);
        paymentAccount.setId(aliPayAccount.getId());
        paymentAccount.setAcountNumber(aliPayAccount.getAccountNumber());
        return paymentAccount;
    }

    public PaymentAccount transferToPaymentAccount(WeChatAccount weChatAccount) {
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setType(PaymentAccountType.WECHAT_ACCOUNT);
        paymentAccount.setId(weChatAccount.getId());
        paymentAccount.setNickName(weChatAccount.getNickName());
        paymentAccount.setOpenid(weChatAccount.getOpenid());
        return paymentAccount;
    }

    public PaymentAccount transferToPaymentAccount(BankAccount bankAccount) {
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setType(PaymentAccountType.BANK_ACCOUNT);
        paymentAccount.setId(bankAccount.getId());
        paymentAccount.setAccountType(bankAccount.getAccountType());
        paymentAccount.setAcountNumber(bankAccount.getAcountNumber());
        paymentAccount.setBankCode(bankAccount.getBankCode());
        paymentAccount.setHolderName(bankAccount.getHolderName());
        return paymentAccount;
    }

    public BankAccount transferToBankAccount(PaymentAccount paymentAccount) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(paymentAccount.getId());
        bankAccount.setAccountType(paymentAccount.getAccountType());
        bankAccount.setAcountNumber(paymentAccount.getAcountNumber());
        bankAccount.setBankCode(paymentAccount.getBankCode());
        bankAccount.setHolderName(paymentAccount.getHolderName());
        return bankAccount;
    }

    public WeChatAccount transferToWeChatAccount(PaymentAccount paymentAccount){
        WeChatAccount weChatAccount = new WeChatAccount();
        weChatAccount.setId(paymentAccount.getId());
        weChatAccount.setNickName(paymentAccount.getNickName());
        weChatAccount.setOpenid(paymentAccount.getOpenid());
        return weChatAccount;
    }

    public AliPayAccount transferToAliPayAccount(PaymentAccount paymentAccount) {
        AliPayAccount aliPayAccount = new AliPayAccount();
        aliPayAccount.setId(paymentAccount.getId());
        aliPayAccount.setAccountNumber(paymentAccount.getAcountNumber());
        return aliPayAccount;
    }

}
