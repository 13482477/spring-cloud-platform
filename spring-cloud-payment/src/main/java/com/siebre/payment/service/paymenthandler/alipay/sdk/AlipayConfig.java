package com.siebre.payment.service.paymenthandler.alipay.sdk;

/**
 * Created by chunling.yan on 2016/8/5.
 *
 */
public class AlipayConfig {


   /*private static PayParamConfigUtil payParamConfigUtil;
    static {
        ApplicationContext ac = new FileSystemXmlApplicationContext("/spring/applicationContext-bean.xml");
         payParamConfigUtil=  (PayParamConfigUtil)ac.getBean("payParamConfigUtil");
    }*/

	
   // private  static  PayParamConfigUtil payParamConfigUtil= SpringContextUtil.getBean("payParamConfigUtil") ;

	public static String ali_app="ALIPAY_WEB";
    public static String aliwebpayService="create_direct_pay_by_user";
    public static String AlipayTrade = "mobile.securitypay.pay";//app支付接口
    public static String AlipayTradeQuery = "single_trade_query";//支付查询接口
    public static String AlipayRefund = "refund_fastpay_by_platform_nopwd";//app退款接口
    public static String AlipayRefundQuery = "refund_fastpay_query";//退款查询接口
    public static String AlipayPageQuery = "account.page.query";//分页查询接口
    public static String aliwebPayNotifyUrl="http://siebre.51vip.biz:11524/siebre-payment/payment/paymentGateWay/aliPayWebNotify";
    public static String aliwebPayReturnUrl="http://www.aniu.tv";
    

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String input_charset_utf = "utf-8";
    public static String input_charset_gbk = "GBK";

    // 签名方式
    public static String sign_type_rsa = "RSA";
    public static String sign_type_md5 = "MD5";

    //支付类型（1，商品购买）
    public static String payment_type = "1";

    //商品类型(1，实物交易   2，虚拟交易)
    public static String goods_type_1 = "1";
    public static String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
    public  static final String ALIPAY_APP_URL=ALIPAY_GATEWAY_NEW+"_input_charset="+AlipayConfig.input_charset_utf;
    //合作者身份ID
    public static String  partener = "2088701931381014";
    //卖家支付宝账号
    public static String seller_id = "taobao@greenpeaks.com.cn";
    //卖家支付宝账号
    public static String seller_email = "taobao@greenpeaks.com.cn";
    // 商户的私钥
    public static String private_key= "待定";
    // 支付宝的公钥，无需修改该值
    public  static String ali_public_key ="待定";
    //MD5 key值
    public static String MD5_key = "ybfjh4jb6dhm7a3qj62njjgs7ougyffa";
    //付款回调地址
    public  static String notify_url_pay  = "待定";
    //退款回调地址
    public  static String notify_url_refund = "待定";



}
