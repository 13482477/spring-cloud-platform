package com.siebre.payment.paymenthandler.alipay.sdk;

/**
 * 支付宝配置信息
 */
public class AlipayConfig {

    //支付宝支付渠道代码
    public static String CHANNEL_CODE = "ALI_PAY";

    //支付宝-即时到账代码
    public static String WAP_WEB_PAY = "ALIPAY_WEB_PAY";

    //支付宝-手机网关支付代码
    public static String WAP_TRADE_PAY = "ALIPAY_TRADE_WAP_PAY";

    //支付宝-即时到账-支付接口代码
    public static String INTERFACE_FASTPAY = "ALIPAY_FASTPAY";

    //支付宝-即时到账-支付回调接口代码
    public static String INTERFACE_FASTPAY_CALLBACK = "ALIPAY_FASTPAY_CALLBACK";

    //支付宝-即时到账-退款接口代码
    public static String INTERFACE_FASTPAY_REFUND = "ALIPAY_FASTPAY_REFUND";

    //支付宝-手机网关-支付接口代码
    public static String INTERFACE_TRADE_WAPPAY = "ALIPAY_TRADE_WAPPAY";

    //支付宝-手机网关-支付回调接口代码
    public static String INTERFACE_TRADE_CALLBACK = "ALIPAY_TRADE_WAPPAY_CALLBACK";

    //支付宝-手机网关-退款接口代码
    public static String INTERFACE_TRADE_REFUND = "ALIPAY_TRADE_WAPPAY_REFUND";

    //支付宝-手机网关-查询接口代码
    public static String INTERFACE_TRADE_QUERY = "ALIPAY_QUERY";

    //即时到账-支付网关地址
    public static String FASTPAY_REQUEST_URL = "https://mapi.alipay.com/gateway.do";

    //手机网关支付-支付网关地址
    public static String TRADE_REQUEST_URL = "https://openapi.alipay.com/gateway.do";

    //即时到账-支付回调地址
    public static String FASTPAY_CALLBACK_URL = "http://tianci.vicp.io:14369/siebre-cloud-payment-service/payment/paymentGateWay/notify/" + INTERFACE_FASTPAY_CALLBACK;

    //手机网关支付-支付回调地址
    public static String TRADE_CALLBACK_URL = "http://tianci.vicp.io:14369/siebre-cloud-payment-service/payment/paymentGateWay/notify/" + INTERFACE_TRADE_CALLBACK;

    //即时到账-退款回调地址
    public static String FASTPAY_REFUND_CALLBACK_URL = "http://tianci.vicp.io:14369/siebre-cloud-payment-service/payment/paymentGateWay/notify/ALIPAY_FASTPAY_REFUND_CALLBACK";

    //支付同步通知页面地址
    public static String RETURN_PAGE_URL = "http://mobile.siebre.com/siebre-cloud/success";

    public static String WEBPAY_SERVICE="create_direct_pay_by_user";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String INPUT_CHARSET_UTF = "utf-8";

    // 签名方式
    public static String sign_type_rsa = "RSA";
    public static String sign_type_md5 = "MD5";

    //支付类型（1，商品购买）
    public static String PAYMENT_TYPE = "1";

}
