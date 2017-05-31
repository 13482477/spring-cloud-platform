package com.siebre.payment.paymenthandler.baofoo.sdk;

import java.security.MessageDigest;

/**
 * Created by AdamTang on 2017/4/21.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class SecurityUtil {

    /***
     * MD5 加密
     */
    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            byte[] digest = md5.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(Integer.toHexString((digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
