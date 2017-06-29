package com.siebre.payment.paymenthandler.alipay.pay;

import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.entity.enums.SubsequentAction;
import com.siebre.payment.hostconfig.service.PaymentHostConfigService;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipaySign;
import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝--即时到账
 */
@Component("alipayWebPaymentHandler")
public class AlipayWebPaymentHandler extends AbstractPaymentComponent {

	@Autowired
	private PaymentHostConfigService hostConfig;

	@Override
	protected void handleInternal(PaymentRequest request, PaymentResponse response, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction) {
		PaymentOrder paymentOrder = request.getPaymentOrder();

		Map<String, String> paramsMap = this.generateParamsMap(request, paymentWay, paymentInterface, paymentOrder, paymentTransaction);

		this.processSign(paramsMap, paymentWay.getEncryptionMode(), paymentWay.getSecretKey());

		String url = this.getPaymentUrl(paymentWay, paramsMap);

		response.setPayUrl(url);
		response.setReturnCode(ReturnCode.SUCCESS.getDescription());
		response.setReturnMessage("调用成功");
		response.setSubsequentAction(SubsequentAction.REDIRECT_TO_PAYMENT_GATEWAY.getValue());
	}

	private Map<String, String> generateParamsMap(PaymentRequest request, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentOrder paymentOrder,PaymentTransaction paymentTransaction) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", AlipayConfig.WEBPAY_SERVICE);
		params.put("partner", paymentWay.getPaymentChannel().getMerchantCode());
		params.put("_input_charset", AlipayConfig.INPUT_CHARSET_UTF);
		params.put("notify_url", hostConfig.getPaymentHost() + paymentInterface.getCallbackUrl());
		params.put("return_url", hostConfig.getFrontHost() + paymentInterface.getReturnPageUrl() + "?orderNumber=" + paymentOrder.getOrderNumber());
		params.put("payment_type", AlipayConfig.PAYMENT_TYPE);
		params.put("seller_email", paymentWay.getPaymentChannel().getPayeeAccount());
		params.put("subject", "支付体验");
		params.put("out_trade_no", paymentTransaction.getInternalTransactionNumber());
		params.put("total_fee", paymentOrder.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		params.put("it_b_pay", "30m");
		return params;
	}


	private void processSign(Map<String, String> params, EncryptionMode encryptionMode, String secretKey) {
		if (EncryptionMode.MD5.equals(encryptionMode)) {
			String sign = AlipaySign.createMd5SignOrder(params, secretKey);
			logger.info("Alipay sign key generate, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
			params.put("sign", sign);
			params.put("sign_type", AlipayConfig.sign_type_md5);
			return;
		} else if (EncryptionMode.RSA.equals(encryptionMode)) {
			String sign = AlipaySign.createRsaSignOrder(params, secretKey);
			logger.info("Alipay sign key generate, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
			params.put("sign", sign);
			params.put("sign_type", AlipayConfig.sign_type_rsa);
			return;
		}
	}

}
