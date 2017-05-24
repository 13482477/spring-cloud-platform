package com.siebre.payment.restful.paymentgateway.paymentcallback;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.payment.entity.paymentinterface.PaymentInterface;
import com.siebre.payment.restful.basic.BaseController;
import com.siebre.payment.service.paymenthandler.basic.payment.callback.AbstractPaymentCallBackHandler;
import com.siebre.payment.service.paymentway.PaymentWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/payment/paymentGateWay/notify")
public class PaymentCallbackController extends BaseController {

    @Autowired
    private PaymentWayService paymentWayService;

    @RequestMapping(value = "/{notifyCode}", method = {RequestMethod.GET, RequestMethod.POST})
    public Object paymentCallback(@PathVariable String notifyCode, HttpServletRequest request, HttpServletResponse response) {

        PaymentInterface paymentInterface = this.paymentWayService.getNotifyPaymentInterface(notifyCode);

        if (paymentInterface != null) {
            AbstractPaymentCallBackHandler handler = (AbstractPaymentCallBackHandler) SpringContextUtil.getBean(paymentInterface.getHandlerBeanName());

                     return handler.callBackHandle(request, response, paymentInterface);

        }
        return "fail";
    }

}
