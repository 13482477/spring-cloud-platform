package com.siebre.message.test.mapper.paymentway;

import com.siebre.message.test.config.BaseTest;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by meilan on 2017/6/1.
 */
public class PaymentWayMapperTest extends BaseTest {

    @Autowired
    private PaymentWayMapper paymentWayMapper;

    @Test
    public void testAdd(){
        PaymentWay paymentWay = new PaymentWay();
        byte[] cert = new byte[10];
        cert[0] = 1;
        paymentWay.setApiClientCertCer(cert);
        paymentWay.setName("测试");
        paymentWay.setApiClientCertPkcs(cert);
        paymentWayMapper.insert(paymentWay);
        System.out.println(paymentWay);
        Assert.assertNotNull(paymentWay.getId());
    }
}
