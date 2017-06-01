package com.siebre.payment.paymenthandler.wechatpay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.siebre.payment.paymenthandler.wechatpay.sdk.WeChatConfig;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import com.siebre.payment.utils.http.HttpTookit;

/**
 * @author Huang Tianci
 * 微信公众号授权服务
 */
@Service("weChatPublicAuthService")
public class WeChatPublicAuthService {

    @Autowired
    @Qualifier("paymentWayService")
    private PaymentWayService paymentWayService;

    public String getOpenID(String code) {
        PaymentWay paymentWay = this.paymentWayService.getPaymentWayByCode("WECHAT_PUBLIC_PAY");
        /* String ourPageUrl = "http://1z56374v70.51mypc.cn/payment/paymentTestTools"; */
        String redirectUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + paymentWay.getAppId() + "&secret=" + WeChatConfig.weChat_public_appsecret + "&code=" + code + "&grant_type=authorization_code";
        String content = HttpTookit.doGet(redirectUrl,null);
        JSONObject ob = JSON.parseObject(content);
        return (String) ob.get("openid");
    }
}