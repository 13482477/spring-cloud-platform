package com.siebre.payment.paymentinterface.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentinterface.mapper.PaymentInterfaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Huang Tianci
 * 支付接口服务类
 */
@Service("paymentInterfaceService")
public class PaymentInterfaceService {

	@Autowired
	private PaymentInterfaceMapper paymentInterfaceMapper;

	public ServiceResult<List<PaymentInterface>> getPaymentInterfaceListByWayId(Long paymentWayId) {
		List<PaymentInterface> list = this.paymentInterfaceMapper.selectByPaymentWayId(paymentWayId);
		return ServiceResult.<List<PaymentInterface>>builder().success(Boolean.TRUE).data(list).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<List<PaymentInterface>> selectByPage(PageInfo pageInfo) {
		List<PaymentInterface> list = this.paymentInterfaceMapper.selectByPage(pageInfo);
		return ServiceResult.<List<PaymentInterface>>builder().success(Boolean.TRUE).data(list).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<PaymentInterface> createPaymentInterface(PaymentInterface paymentInterface) {
		this.paymentInterfaceMapper.insert(paymentInterface);
		return ServiceResult.<PaymentInterface>builder().success(Boolean.TRUE).data(paymentInterface).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<PaymentInterface> updatePaymentInterface(PaymentInterface paymentInterface) {
		if(paymentInterface.getId() == null) {
			return ServiceResult.<PaymentInterface>builder().success(Boolean.FALSE).data(paymentInterface).message("支付接口主键值为空").build();
		}
		this.paymentInterfaceMapper.updateByPrimaryKeySelective(paymentInterface);
		return ServiceResult.<PaymentInterface>builder().success(Boolean.TRUE).data(paymentInterface).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult<PaymentInterface> selectById(Long id) {
		PaymentInterface paymentInterface = this.paymentInterfaceMapper.selectByPrimaryKey(id);
		return ServiceResult.<PaymentInterface>builder().success(Boolean.TRUE).data(paymentInterface).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

	public ServiceResult deletePaymentInterfaceById(Long id) {
		this.paymentInterfaceMapper.deleteByPrimaryKey(id);
		return ServiceResult.builder().success(Boolean.TRUE).message(ServiceResult.SUCCESS_MESSAGE).build();
	}
}

