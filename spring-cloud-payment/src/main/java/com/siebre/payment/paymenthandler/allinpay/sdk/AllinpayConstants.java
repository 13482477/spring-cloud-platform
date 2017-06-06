package com.siebre.payment.paymenthandler.allinpay.sdk;

/**
 * Created by meilan on 2017/5/27.
 * 通联配置信息
 */
public class AllinpayConstants {
    //通联支付渠道代码
    public static String CHANNEL_CODE = "ALLIN_PAY";

    //通联-实时代扣代码
    public static String ALLIN_REALTIME_PAY = "ALLIN_REALTIME_PAY";

    //通联-实时代扣--支付接口代码
    public static String ALLIN_ACP_PAYMENT = "ALLIN_ACP_PAYMENT";

    //通联-实时代扣--退款接口代码
    public static String ALLIN_ACP_REFUND = "ALLIN_ACP_REFUND";

    //通联-实时代扣--查询接口代码
    public static String ALLIN_ACP_QUERY = "ALLIN_ACP_QUERY";

    //通联-实时代扣--支付/退款/查询请求地址
    public static String ALLIN_URL = "https://113.108.182.3/aipg/ProcessServlet";




}
