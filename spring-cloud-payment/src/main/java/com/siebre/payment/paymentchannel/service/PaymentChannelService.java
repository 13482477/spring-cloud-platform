package com.siebre.payment.paymentchannel.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.mapper.PaymentChannelMapper;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentChannelService {

	@Autowired
	private PaymentChannelMapper paymentChannelMapper;

	@Autowired
	private PaymentWayService paymentWayService;

	@Transactional("db")
	public ServiceResult<PaymentChannel> create(PaymentChannel paymentChannel) {
		this.paymentChannelMapper.insert(paymentChannel);
		return ServiceResult.<PaymentChannel>builder().success(Boolean.TRUE).data(paymentChannel).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<List<PaymentChannel>> searchAll() {
		List<PaymentChannel> data = this.paymentChannelMapper.selectAll();
		return ServiceResult.<List<PaymentChannel>>builder().success(Boolean.TRUE).data(data).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<List<PaymentChannel>> searchAllByPage(PageInfo pageInfo) {
		List<PaymentChannel> data = this.paymentChannelMapper.selectAllByPage(pageInfo);
		return ServiceResult.<List<PaymentChannel>>builder().success(Boolean.TRUE).data(data).message(ServiceResult.SUCCESS_MESSAGE).build();
	}
	
	public PaymentChannel findById(Long id) {
		return this.paymentChannelMapper.selectByPrimaryKey(id);
	}

	public ServiceResult<PaymentChannel> queryById(Long id) {
		PaymentChannel channel = this.paymentChannelMapper.selectByPrimaryKey(id);
		return ServiceResult.<PaymentChannel>builder().success(Boolean.TRUE).data(channel).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<PaymentChannel> queryByChannelCode(String channelCode) {
		PaymentChannel channel = this.paymentChannelMapper.selectByChannelCode(channelCode);
		return ServiceResult.<PaymentChannel>builder().success(Boolean.TRUE).data(channel).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<PaymentChannel> updateById(PaymentChannel paymentChannel) {
		if (paymentChannel.getId() == null) {
			return ServiceResult.<PaymentChannel>builder().success(Boolean.FALSE).message("支付渠道主键为空").build();
		}
		this.paymentChannelMapper.updateByPrimaryKeySelective(paymentChannel);
		return ServiceResult.<PaymentChannel>builder().success(Boolean.TRUE).data(paymentChannel).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	/**
	 * 删除操作，会级联删除支付方式和支付接口，需谨慎
	 *
	 * @return
	 * @Param id 支付渠道id
	 */
	@Transactional("db")
	public ServiceResult deleteByid(Long id) {
		this.paymentChannelMapper.deleteByPrimaryKey(id);
		this.paymentWayService.deletePaymentWayByChannelId(id);
		return ServiceResult.builder().success(Boolean.TRUE).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

}
