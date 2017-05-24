package com.siebre.payment.service.paymenthandler.alipay.sdk;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siebre.payment.utils.http.HttpTookit;
import com.siebre.payment.utils.keypair.RSA;

/**
 * Created by chunling.yan on 2017/03/15. User: chunling.yan des: 生成签名后的订单信息给客户端
 */
public class AlipaySign {

	/**
	 * 支付宝消息验证地址
	 */
	private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";
	private static Logger logger = LoggerFactory.getLogger(AlipaySign.class);

	public static String createRsaSignOrder(Map<String, String> params, String secretKey) {
		String paramStr = creatSign(params);
		logger.info("待签名字符串：" + paramStr);
		// 生成签名
		String sign = RSA.sign(paramStr, secretKey, AlipayConfig.input_charset_utf);
		logger.info("sign=====" + sign);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (Exception e) {
			logger.error("签名编码出错！" + e);
		}
		logger.info("sign URLEncoder========" + sign);
		// 拼装完整参数，返回调用者
		StringBuilder str = new StringBuilder(paramStr);
		str.append("&");
		str.append("sign=" + sign);
		str.append("&");
		str.append("sign_type=" + AlipayConfig.sign_type_rsa);
		logger.info("添加签名后的参数=======" + str.toString());
		return str.toString();
	}

	// 生成签名后的订单信息
	public static String createMd5SignOrder(Map<String, String> params, String secretKey) {
		String prestr = createLinkString(params);
		prestr = prestr + secretKey;
		return DigestUtils.md5Hex(getContentBytes(prestr, AlipayConfig.input_charset_utf));
	}

	/**
	 * 签名字符串
	 * 
	 * @param text
	 *            需要签名的字符串
	 * @param sign
	 *            签名结果
	 * @param key
	 *            密钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 */
	public static boolean verify(String text, String sign, String key, String input_charset) {
		text = text + key;
		String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
		if (mysign.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
		}
	}

	public static String creatSign(Map<String, String> map) {

		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayFunction.paraFilter(map);
		// 获取待签名字符串
		String preSignStr = AlipayFunction.createLinkString(sParaNew);
		return preSignStr;
	}

	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		System.out.println("签名顺序-----------------------------:" + prestr);
		return prestr;
	}

	/**
	 * web付款回调验签
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @return 验证结果
	 */
	public static boolean webPayVerify(Map<String, String> params) {
		String responseTxt = "false";
		if (params == null) {
			return false;
		}
		if (params.get("notify_id") != null) {
			String notify_id = params.get("notify_id");
			String partner = AlipayConfig.partener;
			String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;
			String inputLine = "";
			inputLine = HttpTookit.doGet(veryfy_url, null);
			responseTxt = inputLine;
		}
		String sign = "";
		if (params.get("sign") != null) {
			sign = params.get("sign");
		}
		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayFunction.paraWebFilter(params);
		// 获取待签名字符串
		String preSignStr = AlipayFunction.createLinkString(sParaNew);
		// 获得签名验证结果
		boolean isSign1 = false;
		if (AlipayConfig.sign_type_rsa.equals("RSA")) {
			isSign1 = RSA.verify(preSignStr, sign, AlipayConfig.ali_public_key, AlipayConfig.input_charset_utf);
		}
		boolean isSign = isSign1;
		logger.info("验签结果：" + isSign);
		logger.info("responseTxt:" + responseTxt);

		// 写日志记录（若要调试，请取消下面两行注释）
		// String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign +
		// "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
		// AlipayCore.logResult(sWord);

		if (isSign && responseTxt.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

}
