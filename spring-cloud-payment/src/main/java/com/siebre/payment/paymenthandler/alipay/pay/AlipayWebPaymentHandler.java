package com.siebre.payment.paymenthandler.alipay.pay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipaySign;
import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;

/**
 * 支付宝--即时到账
 */
@Component("alipayWebPaymentHandler")
public class AlipayWebPaymentHandler extends AbstractPaymentComponent {

	@Override
	protected PaymentResponse handleInternal(PaymentRequest request, PaymentWay paymentWay, PaymentOrder paymentOrder, PaymentTransaction paymentTransaction) {
		Map<String, String> paramsMap = this.generateParamsMap(request, paymentWay, paymentTransaction);
		
		this.processSign(paramsMap, paymentWay.getEncryptionMode(), paymentWay.getSecretKey());
		
		String url = this.getPaymentUrl(paymentWay, paramsMap);
		
		return PaymentResponse.builder().payUrl(url).build();
	}
	
	private Map<String, String> generateParamsMap(PaymentRequest request, PaymentWay paymentWay, PaymentTransaction paymentTransaction) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", AlipayConfig.aliwebpayService);
		params.put("partner", paymentWay.getPaymentChannel().getMerchantCode());
		params.put("_input_charset", AlipayConfig.input_charset_utf);
		params.put("notify_url", paymentWay.getPaymentCallbackUrl());
		params.put("return_url", paymentWay.getPaymentReturnPageUrl());
		params.put("payment_type", AlipayConfig.payment_type);
		//TODO
		params.put("seller_email", "zhangqing@siebresystems.com");
		params.put("subject", "保险产品");
		params.put("out_trade_no", paymentTransaction.getInternalTransactionNumber());
		params.put("total_fee", paymentTransaction.getPaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		return params;
	}

	
	private void processSign(Map<String, String> params, EncryptionMode encryptionMode, String secretKey) {
		if (EncryptionMode.MD5.equals(encryptionMode)) {
			String sign = AlipaySign.createMd5SignOrder(params, secretKey);
			logger.info("Alipay sign key generate, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
			params.put("sign", sign);
			params.put("sign_type", AlipayConfig.sign_type_md5);
			return;
		}
		else if (EncryptionMode.RSA.equals(encryptionMode)) {
			String sign = AlipaySign.createRsaSignOrder(params, secretKey);
			logger.info("Alipay sign key generate, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
			params.put("sign", sign);
			params.put("sign_type", AlipayConfig.sign_type_rsa);
			return;
		}
	}

}
