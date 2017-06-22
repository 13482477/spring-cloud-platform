package com.siebre.payment.paymenthandler.wechatpay.pay;

import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.entity.enums.SubsequentAction;
import com.siebre.payment.hostconfig.service.PaymentHostConfigService;
import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymenthandler.wechatpay.sdk.WeChatParamConvert;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.utils.http.HttpTookit;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信扫码支付
 */
@Component("weChatScanPaymentHandler")
public class WeChatScanPaymentHandler extends AbstractPaymentComponent {

	@Autowired
	private PaymentHostConfigService hostConfig;

	@Override
	protected void handleInternal(PaymentRequest request, PaymentResponse response, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction) {

		Map<String, String> params = this.generateParamsMap(request, paymentWay, paymentInterface, paymentTransaction);

		this.processSign(params, paymentWay.getEncryptionMode(), paymentWay.getSecretKey());

		this.getPaymentUrl(response, paymentWay, params);


	}

	private Map<String, String> generateParamsMap(PaymentRequest request, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction) {
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("appid", paymentWay.getAppId()); // appid：每个公众号都有一个appid
		paramMap.put("mch_id", paymentWay.getPaymentChannel().getMerchantCode()); // 商户号：开通微信支付后分配
		// 随机数
		paramMap.put("nonce_str", String.valueOf(UUID.randomUUID()).substring(0, 31));
		paramMap.put("body", "保险产品"); // 商品描述
		// 商户订单号：用户id + “|” + 随机16位字符
		paramMap.put("out_trade_no", paymentTransaction.getInternalTransactionNumber());
		paramMap.put("total_fee", request.getPaymentOrder().getAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString()); // 金额必须为整数
		// 单位为分
		paramMap.put("spbill_create_ip", request.getIp()); // 本机的Ip
		Date current = new Date();
		paramMap.put("time_start", DateFormatUtils.format(current, "yyyyMMddHHmmss")); // 交易起始时间
		paramMap.put("time_expire", DateFormatUtils.format(DateUtils.addMinutes(current,30), "yyyyMMddHHmmss")); // 交易结束时间,设置为起始时间后30分钟
		paramMap.put("notify_url", hostConfig.getPaymentHost() + paymentInterface.getCallbackUrl()); // 支付成功后，回调地址
		paramMap.put("trade_type", "NATIVE"); // 交易类型
		paramMap.put("product_id", paymentTransaction.getInternalTransactionNumber()); // 商户根据自己业务传递的参数
		return paramMap;
	}

	protected void getPaymentUrl(PaymentResponse response, PaymentWay paymentWay, Map<String, String> params) {
		String payXml = ConvertToXML.toXml(params);

		String wechatPaymentUrl = getPaymentGateWayUrl(paymentWay);

		String payResultXml = HttpTookit.doPost(wechatPaymentUrl, payXml);
		Map<String, String> resultMap = ConvertToXML.toMap(payResultXml);

		if("FAIL".equals(resultMap.get("return_code"))) {
			logger.error("获取微信支付地址失败，原因：{}", resultMap.get("return_msg"));
			response.setReturnCode(ReturnCode.FAIL.getDescription());
			response.setReturnMessage(resultMap.get("return_msg"));
		} else {
			String url = resultMap.get("code_url");
			logger.info("WechatScan url generated, url={}", url);
			response.setPayUrl(url);
			response.setReturnCode(ReturnCode.SUCCESS.getDescription());
			response.setSubsequentAction(SubsequentAction.GENERATE_ERWEIMA.getValue());
		}
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
