package com.siebre.basic.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import com.siebre.basic.exception.SiebreRuntimeException;

public class EncryptionUtils {

	public static String encoderByMd5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			Base64 base64 = new Base64();
			return base64.encodeToString(md5.digest(str.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException |  UnsupportedEncodingException e) {
			throw new SiebreRuntimeException(e);
		}
	}

}
