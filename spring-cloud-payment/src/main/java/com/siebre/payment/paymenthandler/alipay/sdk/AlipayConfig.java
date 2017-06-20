package com.siebre.payment.paymenthandler.alipay.sdk;

import com.siebre.payment.paymentcallback.controller.PaymentCallbackController;

/**
 * 支付宝配置信息
 */
public class AlipayConfig {

    //支付宝支付渠道代码
    public static String CHANNEL_CODE = "ALI_PAY";

    //支付宝-即时到账代码
    public static String WAY_WEB_PAY = "ALIPAY_WEB_PAY";

    //支付宝-手机网关支付代码
    public static String WAY_TRADE_PAY = "ALIPAY_TRADE_WAP_PAY";

    //即时到账-支付网关地址
    public static String FASTPAY_REQUEST_URL = "https://mapi.alipay.com/gateway.do";

    //手机网关支付-支付网关地址
    public static String TRADE_REQUEST_URL = "https://openapi.alipay.com/gateway.do";

    //即时到账-支付回调地址
    public static String FASTPAY_CALLBACK_URL = PaymentCallbackController.CALL_BACK_URI + WAY_WEB_PAY;

    //手机网关支付-支付回调地址
    public static String TRADE_CALLBACK_URL = PaymentCallbackController.CALL_BACK_URI + WAY_TRADE_PAY;

    //支付同步通知页面地址
    public static String RETURN_PAGE_URL = "/siebre-cloud/success";

    public static String WEBPAY_SERVICE="create_direct_pay_by_user";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String INPUT_CHARSET_UTF = "utf-8";

    // 签名方式
    public static String sign_type_rsa = "RSA";
    public static String sign_type_md5 = "MD5";

    //支付类型（1，商品购买）
    public static String PAYMENT_TYPE = "1";

}
