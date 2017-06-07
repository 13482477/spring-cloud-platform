package com.siebre.payment.paymentchannel.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentChannelStatus;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.mapper.PaymentChannelMapper;
import com.siebre.payment.paymentchannel.vo.PaymentChannelVo;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenthandler.allinpay.sdk.AllinpayConstants;
import com.siebre.payment.paymenthandler.baofoo.pay.prepay.BaofooCon;
import com.siebre.payment.paymenthandler.baofoo.sdk.BaofooConfig;
import com.siebre.payment.paymenthandler.wechatpay.sdk.WeChatConfig;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

	public ServiceResult<List<PaymentChannelVo>> searchAll() {
		List<PaymentChannelVo> result = initChannelList();
		List<PaymentChannel> data = this.paymentChannelMapper.selectAll();
		Map<String, PaymentChannel> temp = transferToMap(data);
		for (PaymentChannelVo vo: result) {
			if(temp.containsKey(vo.getChannelCode())){
				vo.setStatus(temp.get(vo.getChannelCode()).getStatus());
			}
		}
		return ServiceResult.<List<PaymentChannelVo>>builder().success(Boolean.TRUE).data(result).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	private Map<String,PaymentChannel> transferToMap(List<PaymentChannel> data) {
		Map<String, PaymentChannel> temp = new HashMap<>();
		for (PaymentChannel channel: data) {
			temp.put(channel.getChannelCode(), channel);
		}
		return temp;
	}

	private List<PaymentChannelVo> initChannelList() {
		List<PaymentChannelVo> result = new ArrayList<>();
		result.add(initChannel(AlipayConfig.CHANNEL_CODE, "支付宝"));
		result.add(initChannel(WeChatConfig.CHANNEL_CODE, "微信支付"));
		//TODO 该为配置类的静态属性
		result.add(initChannel("UNION_PAY", "银联支付"));
		result.add(initChannel("QQ_PAY", "QQ支付"));
		result.add(initChannel("JD_PAY", "京东支付"));
		result.add(initChannel("BD_PAY", "百度支付"));
		result.add(initChannel("YWT_PAY", "一网通支付"));
		result.add(initChannel(AllinpayConstants.CHANNEL_CODE, "通联支付"));
		result.add(initChannel(BaofooConfig.CHANNEL_CODE, "宝付支付"));
		return result;
	}

	private PaymentChannelVo initChannel(String code, String name) {
		PaymentChannelVo vo = new PaymentChannelVo();
		vo.setChannelCode(code);
		vo.setChannelName(name);
		vo.setStatus(PaymentChannelStatus.DISABLE);
		return vo;
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
