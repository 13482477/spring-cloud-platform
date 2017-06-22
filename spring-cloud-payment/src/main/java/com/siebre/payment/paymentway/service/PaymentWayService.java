package com.siebre.payment.paymentway.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentinterface.mapper.PaymentInterfaceMapper;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("paymentWayService")
public class PaymentWayService {

	@Autowired
	private PaymentWayMapper paymentWayMapper;

	@Autowired
	private PaymentInterfaceMapper paymentInterfaceMapper;

	public PaymentWay getPaymentWay(String paymentWayCode) {
		return this.paymentWayMapper.getPaymentWayByCode(paymentWayCode);
	}

	public List<PaymentWay> getPaymentWayByChannelId(Long channelId) {
		return this.paymentWayMapper.getPaymentWayByChannelId(channelId);
	}

	public ServiceResult<PaymentWay> getPaymentWayByCode(String paymentWayCode) {
		PaymentWay data = this.paymentWayMapper.getPaymentWayByCode(paymentWayCode);
		return ServiceResult.<PaymentWay>builder().success(true).data(data).build();
	}

	public PaymentInterface getPaymentInterface(String paymentWayCode, PaymentInterfaceType paymentInterfaceType) {
		PaymentWay paymentWay = this.paymentWayMapper.getPaymentWayByCode(paymentWayCode);
		for (PaymentInterface paymentInterface : paymentWay.getPaymentInterfaces()) {
			if (paymentInterfaceType != null && paymentInterfaceType.equals(paymentInterface.getPaymentInterfaceType())) {
				return paymentInterface;
			}
		}
		return null;
	}

	public ServiceResult<List<PaymentWay>> selectAllByPage(PageInfo pageInfo) {
		List<PaymentWay> list = this.paymentWayMapper.selectAllByPage(pageInfo);
		return ServiceResult.<List<PaymentWay>>builder().success(Boolean.TRUE).data(list).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<PaymentWay> createPaymentWay(PaymentWay paymentWay) {
		this.paymentWayMapper.insert(paymentWay);
		return ServiceResult.<PaymentWay>builder().success(Boolean.TRUE).data(paymentWay).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<PaymentWay> updatePaymentWay(PaymentWay paymentWay) {
		if (paymentWay.getId() == null) {
			return ServiceResult.<PaymentWay>builder().success(Boolean.FALSE).data(paymentWay).message("支付方式主键值为空").build();
		}
		this.paymentWayMapper.updateByPrimaryKeySelective(paymentWay);
		return ServiceResult.<PaymentWay>builder().success(Boolean.TRUE).data(paymentWay).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<PaymentWay> getPaymentWayById(Long id) {
		PaymentWay paymentWay = this.paymentWayMapper.selectByPrimaryKey(id);
		return ServiceResult.<PaymentWay>builder().success(Boolean.TRUE).data(paymentWay).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	/**
	 * 会级联删除该支付渠道下的支付接口，请谨慎
	 * @param id
	 * @return
	 */
	@Transactional("db")
	public ServiceResult deletePaymentWayById(Long id) {
		this.paymentWayMapper.deleteByPrimaryKey(id);
		this.paymentInterfaceMapper.deleteByPaymentWayId(id);
		return ServiceResult.builder().success(Boolean.TRUE).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	/**
	 * 根据channelId删除paymentWay，该方法会级联删除支付接口,需谨慎
	 * @param channelId 支付渠道id
	 * @return
	 */
	@Transactional("db")
	public ServiceResult deletePaymentWayByChannelId(Long channelId) {
		List<PaymentWay> wayList = this.paymentWayMapper.getPaymentWayByChannelId(channelId);
		for (PaymentWay paymentWay: wayList) {
			this.paymentInterfaceMapper.deleteByPaymentWayId(paymentWay.getId());
		}
		this.paymentWayMapper.deleteByChannelId(channelId);
		return ServiceResult.builder().success(Boolean.TRUE).message(ServiceResult.SUCCESS_MESSAGE).build();
	}
}
