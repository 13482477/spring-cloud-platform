package com.siebre.payment.paymenthandler.wechatpay.sdk;

public class WeChatConfig {

    public static final String CHANNEL_CODE = "WECHAT_PAY";

    //微信扫码支付
    public static final String WAY_SCAN_PAY = "WECHAT_SCAN_PAY";

    //微信公众号支付
    public static final String WAY_PUBLIC_PAY = "WECHAT_PUBLIC_PAY";

    //微信扫码支付支付接口
    public static final String INTERFACE_SCAN_PAY = "WECHAT_SCAN_PAYMENT";

    //微信扫码支付支付回调接口
    public static final String INTERFACE_SACN_PAY_CALLBACK = "WECHAT_NOTIFY";

    //查询接口代码
    public static final String INTERFACE_ORDER_QUERY = "WECHAT_ORDER_QUERY";

    //微信公众号支付代码
    public static final String INTERFACE_PUBLIC_PAY = "WECHAT_PUBLIC_PAYMENT";

    //微信公众好支付回调代码
    public static final String INTERFACE_PUBLIC_PAY_CALLBACK = "WECHAT_PUBLIC_NOTIFY";

    //微信统一退款接口
    public static final String INTERFACE_REFUND = "WECHAT_REFUND";

    //统一支付网关
    public static final String PAY_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //统一查询网关地址
    public static final String QUERY_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    //扫码支付回调地址
    public static final String SCAN_CALLBACK_URL = "http://tianci.vicp.io:14369/siebre-cloud-payment-service/payment/paymentGateWay/notify/" + INTERFACE_SACN_PAY_CALLBACK;

    //公众号支付回调地址
    public static final String PUBLIC_CALLBACK_URL = "http://tianci.vicp.io:14369/siebre-cloud-payment-service/payment/paymentGateWay/notify/" + INTERFACE_PUBLIC_PAY_CALLBACK;

    //同步回调地址
    public static final String RETURN_PAGE_URL = "http://mobile.siebre.com/siebre-cloud/success";

    //统一退款接口
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    public static final String WECHAT_PUBLIC_APPSECRET = "c58d7665b25130a281339ba98bb5269f";

}

