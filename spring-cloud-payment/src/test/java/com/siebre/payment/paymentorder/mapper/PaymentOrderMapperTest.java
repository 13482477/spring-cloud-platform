package com.siebre.payment.paymentorder.mapper;

import com.siebre.base.DbTestConfig;
import com.siebre.payment.entity.enums.PaymentOrderCheckStatus;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentOrderRefundStatus;
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
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
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
        paymentOrder.setRefundStatus(PaymentOrderRefundStatus.NOT_REFUND);

        return paymentOrder;
    }

    private void assertAllFiledTrue(PaymentOrder paymentOrder, PaymentOrder order) {
        assertTrue(order.getPaymentChannelId().equals(paymentOrder.getPaymentChannelId()));
        assertTrue(order.getOrderNumber().equals(paymentOrder.getOrderNumber()));
        assertTrue(order.getAmount().equals(paymentOrder.getAmount()));
        assertTrue(order.getCheckTime().equals(paymentOrder.getCheckTime()));
        assertTrue(order.getCheckStatus().equals(paymentOrder.getCheckStatus()));
        assertTrue(order.getStatus().equals(paymentOrder.getStatus()));
        assertTrue(paymentOrder.getRefundStatus().equals(paymentOrder.getRefundStatus()));
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test(){
        String s = "2017-05-19T13:26:25";
        new Date(s);
    }

    @Test
    public void deleteByPrimaryKey() throws Exception {

    }

    @Test
    @Transactional
    public void insert() throws Exception {
        PaymentOrder paymentOrder = newPaymentOrder();
        paymentOrderMapper.insert(paymentOrder);
        PaymentOrder order = paymentOrderMapper.selectByPrimaryKey(paymentOrder.getId());
        assertAllFiledTrue(paymentOrder,order);
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

    @Test
    public void selectByOrderNumber() throws Exception {

    }

    @Test
    public void selectByMessageId() throws Exception {

    }

    @Test
    public void selectByOrderNumberleftjoin() throws Exception {

    }

    @Test
    public void selectOrderByPage() throws Exception {

    }

    @Test
    public void selectOrderJoinTransaction() throws Exception {

    }

    @Test
    public void selectOrderSummery() throws Exception {

    }

    @Test
    public void updateOrderStatusToClose() throws Exception {

    }

    @Test
    public void getSuccessedPaymentAmount() throws Exception {

    }

    @Test
    public void getSuccessedPaymentCount() throws Exception {

    }

    @Test
    public void getFaildPaymentAmount() throws Exception {

    }

    @Test
    public void getFaildPaymentCount() throws Exception {

    }

    @Test
    public void getCount() throws Exception {

    }

    @Test
    public void getTotalAmountByDateRange() throws Exception {

    }

    @Test
    public void getChannelSuccessedCount() throws Exception {

    }

    @Test
    public void getChannelFailCount() throws Exception {

    }

    @Test
    public void getChannelSuccessedAmount() throws Exception {

    }

    @Test
    public void getChannelFailAmount() throws Exception {

    }

    @Test
    public void getPaymentWayCount() throws Exception {

    }

    @Test
    public void countPaymentChannelTransaction() throws Exception {

    }

    @Test
    public void getOrdersByChannelAndDate() throws Exception {

    }

}