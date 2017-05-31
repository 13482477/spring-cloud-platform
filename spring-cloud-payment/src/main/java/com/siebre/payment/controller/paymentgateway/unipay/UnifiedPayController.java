package com.siebre.payment.controller.paymentgateway.unipay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import com.siebre.payment.service.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.service.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.service.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.service.paymenthandler.wechatpay.WeChatPublicAuthService;

@RestController
public class UnifiedPayController {

    @Autowired
    private PaymentWayService paymentWayService;

    @Autowired
    @Qualifier("weChatPublicAuthService")
    private WeChatPublicAuthService weChatPublicAuthService;

    @RequestMapping(value = "/paymentGateway/unifiedPay", method = {RequestMethod.POST})
    @ResponseBody
    public UnifiedPayResponse unipay(@RequestBody UnifiedPayRequest unipayRequest, HttpServletRequest request) {
    	PaymentRequest paymentRequest = new PaymentRequest();
    	paymentRequest.setPaymentWayCode(unipayRequest.getPayWayCode());
    	paymentRequest.setOrderNumber(unipayRequest.getOrderNumber());
    	paymentRequest.setPaymentOrderItems(unipayRequest.getPaymentOrderItems());
    	paymentRequest.setIp(null);
        paymentRequest.setOpenid(unipayRequest.getOpenid());
    	
    	PaymentInterface paymentInterface = this.paymentWayService.getPaymentInterface(paymentRequest.getPaymentWayCode(), PaymentInterfaceType.PAY);
    	
        AbstractPaymentComponent paymentComponent = (AbstractPaymentComponent) SpringContextUtil.getBean(paymentInterface.getHandlerBeanName());
        
        PaymentResponse paymentResponse = paymentComponent.handle(paymentRequest);
        
        UnifiedPayResponse response = new UnifiedPayResponse();
        response.setPaymentUrl(paymentResponse.getPayUrl());
        response.setBody(paymentResponse.getBody());
        return response;
    }

    /**
     * 获取请求微信授权的url
     * pageUrl  微信需要重定向的目标页面，会将code返回回来
     * @param request
     * @return
     */
    @RequestMapping(value = "/paymentGateway/unifiedPay/wechatauthorize", method = {RequestMethod.GET})
    @ResponseBody
    public Object weChatTest(HttpServletRequest request){
        String pageUrl = request.getParameter("pageUrl");
        Map<String,String> result = new HashMap<>();
        PaymentWay paymentWay = this.paymentWayService.getPaymentWayByCode("WECHAT_PUBLIC_PAY");
        String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ paymentWay.getAppId() +"&redirect_uri=" + pageUrl +
                "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
        result.put("redirectUrl",redirectUrl);
        return result;
    }

    /**
     * 获取微信openid
     * @param request
     * @return
     */
    @RequestMapping(value = "/paymentGateway/unifiedPay/getWeChatOpenid",method = {RequestMethod.GET})
    @ResponseBody
    public Object getWeChatOpenid(HttpServletRequest request){
        Map<String,String> result = new HashMap<>();
        String openid = weChatPublicAuthService.getOpenID(request.getParameter("code"));
        result.put("openid",openid);
        return result;
    }

}
