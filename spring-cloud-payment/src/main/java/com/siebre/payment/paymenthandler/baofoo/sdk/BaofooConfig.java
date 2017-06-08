package com.siebre.payment.paymenthandler.baofoo.sdk;

/**
 * Created by meilan on 2017/6/2.
 * 宝付配置信息
 */
public class BaofooConfig {
    //宝付支付渠道代码
    public static String CHANNEL_CODE = "BAOFOO_PAY";

    //快捷支付
    public static String WAY_BAOFOO_FAST_PAY = "BAOFOO_FAST_PAY";

    //代扣
    public static String WAY_BAOFOO_WITHHOLDING = "BAOFOO_WITHHOLDING";

    //宝付-快捷支付-预支付请求地址
    public static String BAOFOO_FASTPAY_URL = "https://gw.baofoo.com/quickpay/api/prepareorder";

}
