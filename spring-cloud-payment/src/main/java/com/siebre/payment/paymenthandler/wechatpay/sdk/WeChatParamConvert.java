package com.siebre.payment.paymenthandler.wechatpay.sdk;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;

public class WeChatParamConvert {

	public static HashMap<String, String> weiXinWebpayParamConvert(PaymentRequest paymentRequest, PaymentWay paymentWay, PaymentTransaction paymentTransaction) {

		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("appid", paymentWay.getAppId()); // appid：每个公众号都有一个appid
		paramMap.put("mch_id", paymentWay.getPaymentChannel().getMerchantCode()); // 商户号：开通微信支付后分配
		// 随机数
		paramMap.put("nonce_str", String.valueOf(UUID.randomUUID()).substring(0, 31));
		paramMap.put("body", "保险产品"); // 商品描述
		// 商户订单号：用户id + “|” + 随机16位字符
		paramMap.put("out_trade_no", paymentTransaction.getInternalTransactionNumber());
		paramMap.put("total_fee", paymentTransaction.getPaymentAmount().toString()); // 金额必须为整数 单位为分
		paramMap.put("spbill_create_ip", paymentRequest.getIp()); // 本机的Ip
		paramMap.put("notify_url", WeChatConfig.weiXinwebPayNotifyUrl); // 支付成功后，回调地址
		paramMap.put("trade_type", "NATIVE"); // 交易类型
		paramMap.put("product_id", "100001"); // 商户根据自己业务传递的参数
												// 当trade_type=NATIVE时必填
		// 根据微信签名规则，生成签名。随机参数可以在商户后台管理系统中进行设置。
		paramMap.put("sign", signMd5(paramMap, WeChatConfig.weixinSecritKey));
		return paramMap;
	}

	public static Map<String, Object> toMap(Object object) {
		if (object == null) {
			return null;
		}
		if ((object instanceof java.util.Map)) {
			if ((object instanceof TreeMap)) {
				return (TreeMap) object;
			}
			return new TreeMap((java.util.Map) object);
		}

		java.util.Map<String, Object> map = new TreeMap();

		Class cls = object.getClass();
		while (cls != Object.class) {
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				try {
					if (!Modifier.isStatic(field.getModifiers())) {
						field.setAccessible(true);
						Object value = field.get(object);
						if (value != null) {
							map.put(field.getName(), value);
						}
					}
				} catch (Exception localException) {
				}
			}
			cls = cls.getSuperclass();
		}
		return map;
	}

	public static String prepareSignStr(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());

		Collections.sort(keys);

		List<String> keyValues = new ArrayList<String>();

		for (String key : keys) {
			keyValues.add(key + "=" + params.get(key));
		}
		return StringUtils.join(keyValues, "&");
	}

	public static String signMd5(Map<String, String> object, String key) {
		String needSignStr = prepareSignStr(object);
		needSignStr = needSignStr + "&key=" + key;
		return DigestUtils.md5Hex(needSignStr).toUpperCase();
	}

	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

}
