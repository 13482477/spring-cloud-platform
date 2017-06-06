package com.siebre.payment.paymenthandler.baofoo.sdk;

/**
 * Created by meilan on 2017/6/2.
 * 宝付配置信息
 */
public class BaofooConfig {
    //宝付支付渠道代码
    public static String CHANNEL_CODE = "BAOFOO_PAY";

    //宝付-快捷支付代码
    public static String BAOFOO_FAST_PAY = "BAOFOO_FAST_PAY";

    //宝付-快捷支付预支付代码
    public static String BAOFOO_FASTPAY_PREPAY = "BAOFOO_FASTPAY_PREPAY";

    //宝付-快捷支付-预支付请求地址
    public static String BAOFOO_FASTPAY_URL = "https://gw.baofoo.com/quickpay/api/prepareorder";

}
