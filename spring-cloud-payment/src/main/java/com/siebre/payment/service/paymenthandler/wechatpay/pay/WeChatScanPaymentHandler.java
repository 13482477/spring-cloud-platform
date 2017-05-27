package com.siebre.payment.service.paymenthandler.wechatpay.pay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.siebre.payment.service.paymenthandler.wechatpay.sdk.WeChatParamConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.paymentorder.entiry.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.service.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.service.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.service.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.utils.http.HttpTookit;
import com.siebre.payment.utils.messageconvert.ConvertToXML;

/**
 * 微信扫码支付
 */
@Component("weChatScanPaymentHandler")
public class WeChatScanPaymentHandler extends AbstractPaymentComponent {

	@Override
	protected PaymentResponse handleInternal(PaymentRequest request, PaymentWay paymentWay, PaymentOrder paymentOrder, PaymentTransaction paymentTransaction) {

		Map<String, String> params = this.generateParamsMap(request, paymentWay, paymentTransaction);
		
		this.processSign(params, paymentWay.getEncryptionMode(), paymentWay.getSecretKey());
		
		String paymentUrl = this.getPaymentUrl(paymentWay, params);

		return PaymentResponse.builder().payUrl(paymentUrl).build();
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
		paramMap.put("spbill_create_ip", request.getIp() == null ? "116.231.143.139" : request.getIp()); // 本机的Ip
		paramMap.put("notify_url", paymentWay.getPaymentCallbackUrl()); // 支付成功后，回调地址
		paramMap.put("trade_type", "NATIVE"); // 交易类型
		paramMap.put("product_id", paymentTransaction.getInternalTransactionNumber()); // 商户根据自己业务传递的参数
		return paramMap;
	}

	@Override
	protected String getPaymentUrl(PaymentWay paymentWay, Map<String, String> params) {
		String payXml = ConvertToXML.toXml(params);
		
		String wechatPaymentUrl = getPaymentGateWayUrl(paymentWay);
		
		String payResultXml = HttpTookit.doPost(wechatPaymentUrl, payXml);
		Map<String, String> resultMap = ConvertToXML.toMap(payResultXml);

		//TODO xml异常错误处理

		String url = resultMap.get("code_url");
		logger.info("WechatScan url generated, url={}", url);

		return url;
	}


	private void processSign(Map<String, String> params, EncryptionMode encryptionMode, String secretKey) {
		if (EncryptionMode.MD5.equals(encryptionMode)) {
			String sign = WeChatParamConvert.signMd5(params, secretKey);
			logger.info("Wechat sign key generated, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
			params.put("sign", sign);
			return;
		} 
	}

}
