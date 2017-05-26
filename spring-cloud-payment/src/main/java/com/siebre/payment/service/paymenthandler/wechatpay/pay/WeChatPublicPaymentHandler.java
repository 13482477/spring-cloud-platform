package com.siebre.payment.service.paymenthandler.wechatpay.pay;

import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.entity.paymenttransaction.PaymentTransaction;
import com.siebre.payment.entity.paymentway.PaymentWay;
import com.siebre.payment.service.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.service.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.service.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.service.paymenthandler.wechatpay.sdk.WeChatParamConvert;
import com.siebre.payment.utils.http.HttpTookit;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Huang Tianci
 * 微信公众号支付
 */
@Component("weChatPublicPaymentHandler")
public class WeChatPublicPaymentHandler extends AbstractPaymentComponent {

    @Override
    protected PaymentResponse handleInternal(PaymentRequest request, PaymentWay paymentWay, PaymentOrder paymentOrder, PaymentTransaction paymentTransaction) {
        //拼装请求参数
        Map<String, String> params = this.generateParamsMap(request, paymentWay, paymentTransaction);
        //调用统一下单API 获得预付单信息prepay_id
        this.processSign(params, paymentWay.getEncryptionMode(), paymentWay.getSecretKey());
        String prepayId = this.getPrepayId(paymentWay,params);
        //生成JSAPI页面调用的支付参数并签名
        Map<String, String> jsApiParams = generateJsapiParams(paymentWay,prepayId);
        this.processSign2(jsApiParams, paymentWay.getEncryptionMode(), paymentWay.getSecretKey());
        return PaymentResponse.builder().body(jsApiParams).build();
    }

    private Map<String, String> generateJsapiParams(PaymentWay paymentWay, String prepayId) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("appId", paymentWay.getAppId());
        paramMap.put("timeStamp",Long.valueOf(new Date().getTime()).toString());
        paramMap.put("nonceStr",String.valueOf(UUID.randomUUID()).substring(0, 31));
        StringBuilder sb = new StringBuilder().append("prepay_id=").append(prepayId);
        paramMap.put("package",sb.toString());
        paramMap.put("signType",paymentWay.getEncryptionMode().getDescription());
        return paramMap;
    }


    private Map<String, String> generateParamsMap(PaymentRequest request, PaymentWay paymentWay, PaymentTransaction paymentTransaction) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", paymentWay.getAppId()); // appid：每个公众号都有一个appid
        paramMap.put("mch_id", paymentWay.getPaymentChannel().getMerchantCode()); // 商户号：开通微信支付后分配
        // 随机数
        paramMap.put("nonce_str", String.valueOf(UUID.randomUUID()).substring(0, 31));
        paramMap.put("body", "保险产品"); // 商品描述
        // 商户订单号：用户id + “|” + 随机16位字符
        paramMap.put("out_trade_no", paymentTransaction.getInternalTransactionNumber());
        paramMap.put("total_fee", paymentTransaction.getPaymentAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString()); // 金额必须为整数
        // 单位为分
        paramMap.put("spbill_create_ip", request.getIp()); // 用户端ip
        paramMap.put("notify_url", paymentWay.getPaymentCallbackUrl()); // 支付成功后，回调地址
        paramMap.put("trade_type", "JSAPI"); // 交易类型
        paramMap.put("openid", request.getOpenid()); // 交易类型为JSAPI时，必传的参数
        return paramMap;
    }

    private String getPrepayId(PaymentWay paymentWay, Map<String, String> params) {
        String payXml = ConvertToXML.toXml(params);

        //获取统一下单API的URL
        String requestUrl = getPaymentGateWayUrl(paymentWay);

        String prepayOrderXml = HttpTookit.doPost(requestUrl, payXml);
        Map<String, String> resultMap = ConvertToXML.toMap(prepayOrderXml);

        //TODO xml异常错误处理

        String prepayId = resultMap.get("prepay_id");
        logger.info("WechatScan url generated, prepayId={}", prepayId);

        return prepayId;
    }

    private void processSign(Map<String, String> params, EncryptionMode encryptionMode, String secretKey) {
        if (EncryptionMode.MD5.equals(encryptionMode)) {
            String sign = WeChatParamConvert.signMd5(params, secretKey);
            logger.info("Wechat sign key generated, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
            params.put("sign", sign);
            return;
        }
    }
    private void processSign2(Map<String, String> params, EncryptionMode encryptionMode, String secretKey) {
        if (EncryptionMode.MD5.equals(encryptionMode)) {
            String sign = WeChatParamConvert.signMd5(params, secretKey);
            logger.info("Wechat sign key generated, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
            params.put("paySign", sign);
            return;
        }
    }
}
