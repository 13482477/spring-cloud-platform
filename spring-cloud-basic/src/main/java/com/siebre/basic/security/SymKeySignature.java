package com.siebre.basic.security;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author 李志强
 *
 */
public class SymKeySignature {
	private static Logger logger = LoggerFactory.getLogger(SymKeySignature.class);

	private static Base32 base32 = new Base32();

	private static String secretKey = "redstar-yt";
	private static int curSecretKeyVersion = 1;

	private static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map map = new HashMap();
		for (String param : params) {
			String[] splits = param.split("=");
			if (splits.length != 2) {
				logger.warn("malformed params in signature: %s", query);
				return null;
			}
			map.put(splits[0], splits[1]);
		}
		return map;
	}

	public static String signUri(String uri, String userId, boolean useTimeStamp) throws Exception {
		long timeSecs;
		if (useTimeStamp)
			timeSecs = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime());
		else {
			timeSecs = 0L;
		}

		String plainText = String.format("%s&u=%s&t=%d&kv=%d", new Object[] { uri, userId, Long.valueOf(timeSecs), Integer.valueOf(curSecretKeyVersion) });

		byte[] bin = HmacUtils.hmacSha1(secretKey, plainText);
		String sigStr = String.format("t=%d&kv=%d&sig=%s", new Object[] { Long.valueOf(timeSecs), Integer.valueOf(curSecretKeyVersion), StringUtils.newStringUtf8(bin) });

		return String.format("%s&sig=%s", new Object[] { uri, StringUtils.newStringUtf8(base32.encode(StringUtils.getBytesUtf8(sigStr))) });
	}

	public static boolean checkSignedUri(String uri, String userId) {
		try {
			String[] splits = uri.split("&sig=", 2);
			if (splits.length < 2) {
				return false;
			}
			String[] splits2 = splits[1].split("&", 2);

			String sigStr = StringUtils.newStringUtf8(base32.decode(splits2[0]));

			String[] rawSplits = sigStr.split("&sig=", 2);
			if (rawSplits.length < 2) {
				return false;
			}

			Map params = getQueryMap(rawSplits[0]);
			if (params == null) {
				return false;
			}
			String plainText = String.format("%s&u=%s&t=%d&kv=%d",
					new Object[] { splits[0], userId, Long.valueOf(Long.parseLong((String) params.get("t"))), Integer.valueOf(Integer.parseInt((String) params.get("kv"))) });

			byte[] bin = HmacUtils.hmacSha1(secretKey, plainText);
			if (rawSplits[1].equals(StringUtils.newStringUtf8(bin))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.warn("Exception: " + e.getMessage());
		}
		return false;
	}
}