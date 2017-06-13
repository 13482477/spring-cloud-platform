package com.siebre.payment.paymentorder.mapper;

import com.siebre.base.DbTestConfig;
import com.siebre.basic.query.PageInfo;
import com.siebre.payment.entity.enums.*;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by meilan on 2017/6/7.
 */
public class PaymentOrderMapperTest extends DbTestConfig {

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    private PaymentOrder newPaymentOrder() {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentChannelId(Long.valueOf("1"));
        paymentOrder.setOrderNumber("TEST201706071358");
        paymentOrder.setAmount(new BigDecimal("0.01"));
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = "2017-06-07";
        Date date = null;
        try {
            date = fmt.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        paymentOrder.setCheckTime(date);
        paymentOrder.setCheckStatus(PaymentOrderCheckStatus.SUCCESS);
        paymentOrder.setStatus(PaymentOrderPayStatus.PAID);
        paymentOrder.setLockStatus(PaymentOrderLockStatus.UNLOCK);
        //paymentOrder.setRefundStatus(PaymentOrderRefundStatus.NOT_REFUND);

        paymentOrder.setPaymentMethod(PaymentMethod.NET_GATEWAY);
        paymentOrder.setPaymentProvider(PaymentProvider.ALIPAY);
        paymentOrder.setCurrency("CNY");
        paymentOrder.setPaymentAccountId(1L);
        paymentOrder.setSummary("KKKKKK");

        paymentOrder.setNotificationUrl("http://gsajdka.com");
        paymentOrder.setExternalOrderNumber("EXhjkasd789698");

        return paymentOrder;
    }

    private void assertAllFiledTrue(PaymentOrder paymentOrder, PaymentOrder order) {
        assertTrue(order.getPaymentChannelId().equals(paymentOrder.getPaymentChannelId()));
        assertTrue(order.getOrderNumber().equals(paymentOrder.getOrderNumber()));
        assertTrue(order.getAmount().equals(paymentOrder.getAmount()));
        assertTrue(order.getCheckTime().equals(paymentOrder.getCheckTime()));
        assertTrue(order.getCheckStatus().equals(paymentOrder.getCheckStatus()));
        assertTrue(order.getStatus().equals(paymentOrder.getStatus()));
        assertTrue(order.getPaymentMethod().equals(paymentOrder.getPaymentMethod()));
        assertTrue(order.getPaymentProvider().equals(paymentOrder.getPaymentProvider()));
        assertTrue(order.getCurrency().equals(paymentOrder.getCurrency()));
        assertTrue(order.getPaymentAccountId().equals(paymentOrder.getPaymentAccountId()));
        assertTrue(order.getSummary().equals(paymentOrder.getSummary()));
        assertTrue(order.getNotificationUrl().equals(paymentOrder.getNotificationUrl()));
        assertTrue(order.getExternalOrderNumber().equals(paymentOrder.getExternalOrderNumber()));
    }

    @Test
    @Transactional
    public void deleteByPrimaryKey() throws Exception {

    }

    @Test
    @Transactional
    public void insert() throws Exception {
        PaymentOrder paymentOrder = newPaymentOrder();
        paymentOrderMapper.insert(paymentOrder);
        PaymentOrder order = paymentOrderMapper.selectByPrimaryKey(paymentOrder.getId());
        assertAllFiledTrue(paymentOrder, order);
    }

    @Test
    @Transactional
    public void insertSelective() throws Exception {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentMethod(PaymentMethod.NET_GATEWAY);
        paymentOrder.setPaymentProvider(PaymentProvider.ALIPAY);
        paymentOrder.setCurrency("CNY");
        paymentOrder.setPaymentAccountId(1L);
        paymentOrder.setSummary("KKKKKK");
        paymentOrder.setNotificationUrl("http://hdsakj.com");
        paymentOrder.setExternalOrderNumber("EXhjka789236389");
        paymentOrderMapper.insertSelective(paymentOrder);
        PaymentOrder order = paymentOrderMapper.selectByPrimaryKey(paymentOrder.getId());
        assertTrue(order.getPaymentMethod().equals(paymentOrder.getPaymentMethod()));
        assertTrue(order.getPaymentProvider().equals(paymentOrder.getPaymentProvider()));
        assertTrue(order.getCurrency().equals(paymentOrder.getCurrency()));
        assertTrue(order.getPaymentAccountId().equals(paymentOrder.getPaymentAccountId()));
        assertTrue(order.getSummary().equals(paymentOrder.getSummary()));
        assertTrue(order.getNotificationUrl().equals(paymentOrder.getNotificationUrl()));
        assertTrue(order.getExternalOrderNumber().equals(paymentOrder.getExternalOrderNumber()));

        assertNull(order.getAmount());
        assertNull(order.getCheckStatus());
        assertNull(order.getStatus());
    }

    @Test
    @Transactional
    public void selectByPrimaryKey() throws Exception {

    }

    @Test
    @Transactional
    public void updateByPrimaryKeySelective() throws Exception {
        PaymentOrder paymentOrder = newPaymentOrder();
        paymentOrderMapper.insert(paymentOrder);
        PaymentOrder p1 = new PaymentOrder();
        p1.setId(paymentOrder.getId());
        p1.setPaymentMethod(PaymentMethod.PUBLIC_ACCOUNT_PAY);
        p1.setPaymentProvider(PaymentProvider.BAOFOO);
        p1.setCurrency("CNY1");
        p1.setPaymentAccountId(2L);
        p1.setSummary("KKKKddKK");
        p1.setNotificationUrl("http://dahf9798.com");
        p1.setExternalOrderNumber("EX896767");
        paymentOrderMapper.updateByPrimaryKeySelective(p1);
        PaymentOrder order = paymentOrderMapper.selectByPrimaryKey(paymentOrder.getId());

        assertTrue(order.getPaymentMethod().equals(p1.getPaymentMethod()));
        assertTrue(order.getPaymentProvider().equals(p1.getPaymentProvider()));
        assertTrue(order.getCurrency().equals(p1.getCurrency()));
        assertTrue(order.getPaymentAccountId().equals(p1.getPaymentAccountId()));
        assertTrue(order.getSummary().equals(p1.getSummary()));
        assertTrue(order.getNotificationUrl().equals(p1.getNotificationUrl()));
        assertTrue(order.getExternalOrderNumber().equals(p1.getExternalOrderNumber()));

        assertFalse(order.getPaymentMethod().equals(paymentOrder.getPaymentMethod()));
        assertFalse(order.getPaymentProvider().equals(paymentOrder.getPaymentProvider()));
        assertFalse(order.getCurrency().equals(paymentOrder.getCurrency()));
        assertFalse(order.getPaymentAccountId().equals(paymentOrder.getPaymentAccountId()));
        assertFalse(order.getSummary().equals(paymentOrder.getSummary()));
        assertFalse(order.getNotificationUrl().equals(paymentOrder.getNotificationUrl()));
        assertFalse(order.getExternalOrderNumber().equals(paymentOrder.getExternalOrderNumber()));
    }

    @Test
    @Transactional
    public void updateByPrimaryKey() throws Exception {

    }

    @Test
    @Transactional
    public void selectByOrderNumber() throws Exception {

    }

    @Test
    @Transactional
    public void selectByMessageId() throws Exception {

    }

    @Test
    @Transactional
    public void selectByOrderNumberleftjoin() throws Exception {

    }

    @Test
    @Transactional
    public void selectOrderByPage() throws Exception {
        PageInfo page = new PageInfo();
        page.setCurrentPage(1);
        page.setShowCount(10);
        List list =paymentOrderMapper.selectOrderByPage("",null,null,null,null, page);
        System.out.println(list);
    }

    @Test
    @Transactional
    public void selectOrderJoinTransaction() throws Exception {

    }

    @Test
    @Transactional
    public void selectOrderSummery() throws Exception {

    }

    @Test
    @Transactional
    public void updateOrderStatusToClose() throws Exception {

    }

    @Test
    @Transactional
    public void getSuccessedPaymentAmount() throws Exception {

    }

    @Test
    @Transactional
    public void getSuccessedPaymentCount() throws Exception {

    }

    @Test
    @Transactional
    public void getFaildPaymentAmount() throws Exception {

    }

    @Test
    @Transactional
    public void getFaildPaymentCount() throws Exception {

    }

    @Test
    @Transactional
    public void getCount() throws Exception {

    }

    @Test
    @Transactional
    public void getTotalAmountByDateRange() throws Exception {

    }

    @Test
    @Transactional
    public void getChannelSuccessedCount() throws Exception {

    }

    @Test
    @Transactional
    public void getChannelFailCount() throws Exception {

    }

    @Test
    @Transactional
    public void getChannelSuccessedAmount() throws Exception {

    }

    @Test
    @Transactional
    public void getChannelFailAmount() throws Exception {

    }

    @Test
    @Transactional
    public void getPaymentWayCount() throws Exception {

    }

    @Test
    @Transactional
    public void countPaymentChannelTransaction() throws Exception {

    }

    @Test
    @Transactional
    public void getOrdersByChannelAndDate() throws Exception {

    }

}