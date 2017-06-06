package com.siebre.message.test.mapper.paymentchannel;

import com.siebre.message.test.config.BaseTest;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.mapper.PaymentChannelMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by meilan on 2017/6/2.
 */
public class paymentChannelMapperTest extends BaseTest {
    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    @Test
    public void testChannel() {
        PaymentChannel paymentChannel = new PaymentChannel();
        paymentChannel.setChannelName("test");
        paymentChannel.setTerminalId("111111");
        paymentChannelMapper.insert(paymentChannel);
        System.out.println(paymentChannel);
        Assert.assertNotNull(paymentChannel.getId());
    }

}
