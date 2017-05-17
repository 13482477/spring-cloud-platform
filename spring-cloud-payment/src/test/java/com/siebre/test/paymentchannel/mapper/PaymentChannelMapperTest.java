package com.siebre.test.paymentchannel.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.siebre.payment.entity.paymentchannel.PaymentChannel;
import com.siebre.payment.mapper.paymentchannel.PaymentChannelMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
					"classpath:spring/applicationContext-bean.xml", 
					"classpath:spring/applicationContext-jdbc.xml", 
					"classpath:spring/applicationContext-redis.xml", }
)
public class PaymentChannelMapperTest {

	@Autowired
	private PaymentChannelMapper paymentChannelMapper;

	@Test
	public void selectAllTest() {
		List<PaymentChannel> paymentChannels = this.paymentChannelMapper.selectAll();
		System.out.println(paymentChannels.size());
	}

}
