package com.siebre.payment.paymenthandler.wechatpay.sdk;

import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class WeChatParamConvert {

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

}