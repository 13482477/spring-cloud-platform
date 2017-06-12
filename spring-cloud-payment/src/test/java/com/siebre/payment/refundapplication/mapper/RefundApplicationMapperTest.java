package com.siebre.payment.refundapplication.mapper;

import com.siebre.base.DbTestConfig;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by meilan on 2017/6/9.
 * 退款申请
 */
public class RefundApplicationMapperTest extends DbTestConfig {

    @Autowired
    RefundApplicationMapper refundApplicationMapper;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void deleteByPrimaryKey() throws Exception {

    }

    private RefundApplication newRefundApplication() {
        RefundApplication refundApplication = new RefundApplication();
        refundApplication.setOrderNumber("testnumber");
        refundApplication.setRefundAmount(new BigDecimal("0.01"));
        refundApplication.setRefundApplicationNumber("RD123456");
        refundApplication.setMessageId("messageIdTest");
        refundApplication.setNotificationUrl("http://abc.com");
        return refundApplication;
    }

    private void assertAllFiledTrue(RefundApplication refundApplication,RefundApplication application) {
        assertTrue(refundApplication.getOrderNumber().equals(application.getOrderNumber()));
        assertTrue(refundApplication.getRefundAmount().equals(application.getRefundAmount()));
        assertTrue(refundApplication.getRefundApplicationNumber().equals(application.getRefundApplicationNumber()));
        assertTrue(refundApplication.getMessageId().equals(application.getMessageId()));
        assertTrue(refundApplication.getNotificationUrl().equals(application.getNotificationUrl()));
    }

    @Test
    @Transactional
    public void insert() throws Exception {
        RefundApplication refundApplication = newRefundApplication();
        refundApplicationMapper.insert(refundApplication);
        RefundApplication application = refundApplicationMapper.selectByPrimaryKey(refundApplication.getId());
        assertAllFiledTrue(refundApplication,application);
    }

    @Test
    public void insertSelective() throws Exception {

    }

    @Test
    public void selectByPrimaryKey() throws Exception {

    }

    @Test
    public void selectByBusinessNumber() throws Exception {

    }

    @Test
    public void selectByPage() throws Exception {

    }

    @Test
    public void selectRefundList() throws Exception {

    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {

    }

    @Test
    public void updateByPrimaryKey() throws Exception {

    }

}