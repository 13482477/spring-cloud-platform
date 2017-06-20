package com.siebre.payment.paymentcallback.controller;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymenthandler.basic.paymentcallback.AbstractPaymentCallBackHandler;
import com.siebre.payment.paymenthandler.config.HandlerBeanNameConfig;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PaymentCallbackController {

	public static final String CALL_BACK_URI = "/api/v1/paymentGateWay/notify/";

	@Autowired
	private PaymentWayService paymentWayService;

	@RequestMapping(value = CALL_BACK_URI + "{notifyCode}", method = {RequestMethod.POST })
	public WebResult<Object> paymentCallback(@PathVariable String notifyCode, HttpServletRequest request, HttpServletResponse response) {
		String handleBeanName = HandlerBeanNameConfig.CALL_BACK_MAPPING.get(notifyCode);
		PaymentInterface paymentInterface = this.paymentWayService.getPaymentInterface(notifyCode, PaymentInterfaceType.PAY_NOTIFY);
		if (handleBeanName != null) {
			AbstractPaymentCallBackHandler handler = (AbstractPaymentCallBackHandler) SpringContextUtil.getBean(handleBeanName);
			Object result = handler.callBackHandle(request, response, paymentInterface); 
			return WebResult.<Object>builder().returnCode("200").data(result).build();

		}
		return WebResult.<Object>builder().returnCode("500").build();
	}

}
