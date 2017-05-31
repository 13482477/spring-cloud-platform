package com.siebre.payment.paymenthandler.unionpay.sdk;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by AdamTang on 2017/4/14.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 默认采用银联5.1.0版本 SHA-256加密，编码UTF-8
 */
public class UnionPayUtil {
    private static Logger logger = LoggerFactory.getLogger(UnionPayUtil.class);
    /**
     * 签名.
     */
    public static final String PARAM_SIGNATURE = "signature";

    private static final String ALGORITHM_SHA256 = "SHA-256";

    private static final String ENCODING = "UTF-8";


    public static Map<String,String> responseToMap(String response){
        Map<String,String> result = new HashMap<>();
        if(StringUtils.isBlank(response)){
            return result;
        }
        String[] keyValueArray = response.split("&");
        for(String keyValue:keyValueArray){
            String key = keyValue.split("=")[0];
            String value = keyValue.split("=")[1];
            result.put(key,value);
        }
        return result;
    }

    public static Map<String, String> sign(Map<String, String> map, String secureKey) {

        Map<String, String> result = filterEmptyKey(map);

        signBySecureKey(result, secureKey);

        return result;
    }


    public static boolean validateSign(Map<String, String> map, String secureKey){

        if(StringUtils.isBlank(map.get(PARAM_SIGNATURE))){
            return false;
        }

        if (StringUtils.isBlank(secureKey)) {
            logger.error("secureKey is empty");
            return false;
        }

        Map<String, String> result = filterEmptyKey(map);

        String signCode =  result.get(PARAM_SIGNATURE);

        logger.info("原签名串:[" + signCode + "]");

        String stringData = coverMap2String(result);

        String strBeforeSha256 = stringData + "&" + sha256X16Str(secureKey, ENCODING);

        String strAfterSha256 = sha256X16Str(strBeforeSha256, ENCODING);

        return signCode.equals(strAfterSha256);

    }




    private static Map<String, String> filterEmptyKey(Map<String, String> map) {
        logger.info("打印请求报文域 :");
        Map<String, String> result = new HashMap<String, String>();
        Set<String> keySet = map.keySet();

        for (String key : keySet) {
            String value = map.get(key);
            if (StringUtils.isNotBlank(value)) {
                result.put(key, value.trim());// 对value值进行去除前后空处理
                logger.info(key + "-->" + value);
            }
        }
        return result;
    }


    private static String coverMap2String(Map<String, String> data) {
        TreeMap<String, String> tree = new TreeMap<>();
        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            if (PARAM_SIGNATURE.equals(en.getKey().trim())) {
                continue;
            }
            tree.put(en.getKey(), en.getValue());
        }

        it = tree.entrySet().iterator();
        StringBuilder builder = new StringBuilder();

        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            builder.append(en.getKey() + "=" + en.getValue() + "&");
        }

        return builder.substring(0, builder.length() - 1);
    }


    private static boolean signBySecureKey(Map<String, String> data, String secureKey) {

        if (StringUtils.isBlank(secureKey)) {
            logger.error("secureKey is empty");
            return false;
        }

        // 将Map信息转换成key1=value1&key2=value2的形式
        String stringData = coverMap2String(data);

        logger.info("待签名请求报文串:[" + stringData + "]");

        String strBeforeSha256 = stringData + "&" + sha256X16Str(secureKey, ENCODING);
        String strAfterSha256 = sha256X16Str(strBeforeSha256, ENCODING);

        // 设置签名域值
        data.put(PARAM_SIGNATURE, strAfterSha256);

        return true;

    }

    private static String sha256X16Str(String data, String encoding) {
        byte[] bytes = sha256(data, encoding);

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                builder.append("0").append(Integer.toHexString(0xFF & bytes[i]));
            } else {
                builder.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }

        return builder.toString();
    }

    private static byte[] sha256(String data, String encoding) {
        try {
            return sha256(data.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            logger.error("SHA256计算失败", e);
            return new byte[0];
        }
    }

    private static byte[] sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_SHA256);
            md.reset();
            md.update(data);
            return md.digest();
        } catch (Exception e) {
            logger.error("SHA256计算失败", e);
            return new byte[0];
        }
    }

}
