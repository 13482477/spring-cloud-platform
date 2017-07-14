package com.siebre.payment.paymenthandler.wechatpay.sdk;

import com.siebre.payment.paymentcallback.controller.PaymentCallbackController;

public class WeChatConfig {

    public static final String CHANNEL_CODE = "WECHAT_PAY";

    //微信扫码支付
    public static final String WAY_SCAN_PAY = "WECHAT_SCAN_PAY";

    //微信公众号支付
    public static final String WAY_PUBLIC_PAY = "WECHAT_PUBLIC_PAY";

    //统一支付网关
    public static final String PAY_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //统一查询网关地址
    public static final String QUERY_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    //统一查询退款地址
    public static final String REFUND_QUERY_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/refundquery";

    //扫码支付回调地址
    public static final String SCAN_CALLBACK_URL = PaymentCallbackController.CALL_BACK_URI + WAY_SCAN_PAY;

    //公众号支付回调地址
    public static final String PUBLIC_CALLBACK_URL = PaymentCallbackController.CALL_BACK_URI + WAY_PUBLIC_PAY;

    //同步回调地址
    public static final String RETURN_PAGE_URL = "/siebre-cloud/success";

    //统一退款接口
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

}

