package com.siebre.payment.paymenthandler.config;

import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenthandler.allinpay.sdk.AllinpayConfig;
import com.siebre.payment.paymenthandler.baofoo.sdk.BaofooConfig;
import com.siebre.payment.paymenthandler.wechatpay.sdk.WeChatConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Huang Tianci
 *         handler bean name mapping config
 */
public class HandlerBeanNameConfig {

    public static final Map<String, String> PAY_MAPPING = new ConcurrentHashMap<>();

    //支付回调
    public static final Map<String, String> CALL_BACK_MAPPING = new ConcurrentHashMap<>();

    public static final Map<String, String> REFUND_MAPPING = new ConcurrentHashMap<>();

    public static final Map<String, String> QUERY_MAPPING = new ConcurrentHashMap<>();

    //退款查询
    public static final Map<String, String> REFUND_QUERY_MAPPING = new ConcurrentHashMap<>();


    static {
        initPayMapping();
        initCallBackMapping();
        initRefundMapping();
        initQueryMapping();
        initRefundQueryMapping();
    }

    private static void initPayMapping() {
        //支付宝-即时到账
        PAY_MAPPING.put(AlipayConfig.WAY_WEB_PAY, "alipayWebPaymentHandler");
        //支付宝-手机网关
        PAY_MAPPING.put(AlipayConfig.WAY_TRADE_PAY, "alipayTradeWapPaymentHandler");
        //微信-扫码支付
        PAY_MAPPING.put(WeChatConfig.WAY_SCAN_PAY, "weChatScanPaymentHandler");
        //微信-公众号支付
        PAY_MAPPING.put(WeChatConfig.WAY_PUBLIC_PAY, "weChatPublicPaymentHandler");
        //通联实时代扣支付接口
        PAY_MAPPING.put(AllinpayConfig.WAY_ALLIN_REALTIME_PAY, "allinPayRealTimeHandler");
        //宝付快捷支付预支付接口
        PAY_MAPPING.put(BaofooConfig.WAY_BAOFOO_FAST_PAY, "baofooQuickPaymentHandler");
        //宝付代扣支付接口
        PAY_MAPPING.put(BaofooConfig.WAY_BAOFOO_WITHHOLDING, "baofooWithholdingHandler");
        //银联支付网关支付接口
        PAY_MAPPING.put("UNIONPAY_ACP_PAY", "unionPayAcpPaymentHandler");

    }

    private static void initCallBackMapping() {
        //支付宝-即时到账
        CALL_BACK_MAPPING.put(AlipayConfig.WAY_WEB_PAY, "alipayCallBackHandler");
        //支付宝-手机网关
        CALL_BACK_MAPPING.put(AlipayConfig.WAY_TRADE_PAY, "alipayTradeWapCallBackHandler");
        //微信-扫码支付
        CALL_BACK_MAPPING.put(WeChatConfig.WAY_SCAN_PAY, "weChatCallBackHandler");
        //微信-公众号支付
        CALL_BACK_MAPPING.put(WeChatConfig.WAY_PUBLIC_PAY, "weChatPublicCallBackHandler");

        //银联支付回调接口
        CALL_BACK_MAPPING.put("UNIONPAY_ACP_PAY", "unionPayCallBackHandler");
    }

    private static void initRefundMapping() {
        //支付宝-即时到账
        REFUND_MAPPING.put(AlipayConfig.WAY_WEB_PAY, "alipayFastpayRefundHandler");
        //支付宝-手机网关
        REFUND_MAPPING.put(AlipayConfig.WAY_TRADE_PAY, "alipayPaymentRefundHandler");
        //微信-扫码支付
        REFUND_MAPPING.put(WeChatConfig.WAY_SCAN_PAY, "weChatPaymentRefundHandler");
        //微信-公众号支付
        REFUND_MAPPING.put(WeChatConfig.WAY_PUBLIC_PAY, "weChatPaymentRefundHandler");
        //通联实时代扣退款接口
        REFUND_MAPPING.put(AllinpayConfig.WAY_ALLIN_REALTIME_PAY, "allinPayRealTimeRefundHandler");

        //银联退款接口
        REFUND_MAPPING.put("UNIONPAY_ACP_PAY", "unionPayPaymentRefundHandler");
    }

    private static void initQueryMapping() {
        //支付宝-即时到账
        QUERY_MAPPING.put(AlipayConfig.WAY_WEB_PAY, "alipayPaymentQueryHandler");
        //支付宝-手机网关
        QUERY_MAPPING.put(AlipayConfig.WAY_TRADE_PAY, "alipayPaymentQueryHandler");
        //微信-扫码支付
        QUERY_MAPPING.put(WeChatConfig.WAY_SCAN_PAY, "weChatQueryHandler");
        //微信-公众号支付
        QUERY_MAPPING.put(WeChatConfig.WAY_PUBLIC_PAY, "weChatQueryHandler");
        //通联实时代扣查询接口
        QUERY_MAPPING.put(AllinpayConfig.WAY_ALLIN_REALTIME_PAY, "allinPayQueryHandler");

        //银联查询接口
        QUERY_MAPPING.put("UNIONPAY_ACP_PAY", "unionPayPaymentQueryHandler");
    }

    private static void initRefundQueryMapping() {
        //支付宝-即时到账
        REFUND_QUERY_MAPPING.put(AlipayConfig.WAY_WEB_PAY, "alipayPaymentRefundQueryHandler");
        //支付宝-手机网关
        QUERY_MAPPING.put(AlipayConfig.WAY_TRADE_PAY, "alipayPaymentRefundQueryHandler");
    }

}
