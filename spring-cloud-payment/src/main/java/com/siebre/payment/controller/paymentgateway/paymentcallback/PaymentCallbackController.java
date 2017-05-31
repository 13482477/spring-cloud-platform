package com.siebre.payment.controller.paymentgateway.paymentcallback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymenthandler.basic.paymentcallback.AbstractPaymentCallBackHandler;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentway.service.PaymentWayService;

@RestController
public class PaymentCallbackController {

	@Autowired
	private PaymentWayService paymentWayService;

	@RequestMapping(value = "/api/v1/paymentGateWay/notify/{notifyCode}", method = {RequestMethod.POST })
	public WebResult<Object> paymentCallback(@PathVariable String notifyCode, HttpServletRequest request, HttpServletResponse response) {
		PaymentInterface paymentInterface = this.paymentWayService.getNotifyPaymentInterface(notifyCode);
		if (paymentInterface != null) {
			AbstractPaymentCallBackHandler handler = (AbstractPaymentCallBackHandler) SpringContextUtil.getBean(paymentInterface.getHandlerBeanName());
			Object result = handler.callBackHandle(request, response, paymentInterface); 
			return WebResult.<Object>builder().returnCode("200").data(result).build();

		}
		return WebResult.<Object>builder().returnCode("500").build();
	}

}
