package com.siebre.payment.paymenthandler.baofoo.sdk;

import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.paymenthandler.baofoo.pay.prepay.BaofooCon;
import com.siebre.payment.utils.http.HttpTookit;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Component("baofooApiClient")
public class BaofooApiClient {

    private Logger log = LoggerFactory.getLogger(BaofooApiClient.class);

    BaofooCon baofooCon = new BaofooCon();

    public Map<String, String> send(Map<String, Object> requestParams, String url, String payName) {

        Map<String, String> headPostParams = prepareRequestHead(requestParams,payName);

        prepareEncryption(headPostParams, requestParams);

        String postResponse = doPost(headPostParams, url);
        log.info("postResponse ==" + postResponse);

        if(payName.equals("prePay")){//快捷支付预支付
            return processResponseForPrePay(postResponse);
        }else{//代扣支付，快捷支付支付
            return processResponseForPay(postResponse);
        }

    }

    //准备请求头
    private Map<String, String> prepareRequestHead(Map<String, Object> requestParams,String payName) {

        Map<String, String> headPostParams = new HashMap<String, String>();

        headPostParams.put("version", baofooCon.version);// 版本号 绑卡和支付同步完成(4.0.1.0)
        headPostParams.put("terminal_id", requestParams.get("terminal_id").toString());// 终端号
        headPostParams.put("member_id", requestParams.get("member_id").toString());// 商户号
        headPostParams.put("data_type", baofooCon.dataType);// 加密数据类型

        if(payName.equals("prePay")) {//快捷支付
            headPostParams.put("input_charset", baofooCon.inputCharset);// 字符集
        }else {//代扣
            headPostParams.put("txn_type", baofooCon.txnType);//交易类型
            headPostParams.put("txn_sub_type", baofooCon.txnSubType);//交易子类
        }

        return headPostParams;
    }

    //加密数据
    private void prepareEncryption(Map<String, String> HeadPostParam, Map<String, Object> requestParams) {
        // 商户私钥路径
        String pfxPath = this.getClass().getResource("/").getPath()+"baofunew.pfx";
        // 商户私钥加密密码
        String pfxPassword = baofooCon.pfxPassword;

        String dataStr;
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.putAll(requestParams);

        if (HeadPostParam.get("data_type").equals(baofooCon.dataType)) {
            dataStr = ConvertToXML.toXml(dataMap);//map转xml
        } else {
            dataStr = JsonUtil.mapToJson(dataMap);//map转json
        }
        try {
            String base64str = base64Encode(dataStr);//先进行base64加密

            String dataContent = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxPath, pfxPassword);

            HeadPostParam.put("data_content", dataContent);//加密数据
            log.info("data_content == " + dataContent);
            String requestxml = ConvertToXML.toXml(HeadPostParam);//map转xml
            log.info("requestxml == " + requestxml);
        } catch (IOException e) {
            log.error("base64 编码失败", e);
        }
    }

    //执行post请求
    public String doPost(Map<String, String> request, String url) {
        return HttpTookit.doPost(url, request);
    }

    //处理返回数据并解密--快捷支付预支付
    private Map<String, String> processResponseForPrePay(String response) {
        Map<String,String> returnMap = responseValue(response);

        //交易成功（不确定业务是否成功）
        if("0000".equals(returnMap.get("ret_code"))){

            String dataContent = returnMap.get("data_content");
            Map<String,String> map = decryptionDataContent(dataContent);

            if(map!=null) {
                log.info("{\"resp_code\":\"" + map.get("resp_code") + "\","
                        + "\"resp_msg\":\"" + map.get("resp_msg") + "\"}");
            }

            return map;
        }else{
            log.error("{\"resp_code\":\"" + returnMap.get("ret_code") + "\",\"resp_msg\":\""
                    + returnMap.get("ret_msg") + "\"}");

            return returnMap;
        }

    }

    //处理返回数据并解密--代扣,快捷支付支付
    private Map<String, String> processResponseForPay(String response) {

        Map<String,String> resultMap = decryptionDataContent(response);
        log.info("返回码=" + resultMap.get("resp_code") + ",返回信息=" + resultMap.get("resp_msg"));
        if(resultMap.get("resp_code").toString().equals("0000")){
            return resultMap;
        }

        return null;
    }


    //解密数据
    private Map<String, String> decryptionDataContent(String dataContent) {
        String cerPath = this.getClass().getResource("/").getPath()+"baofu.cer";// 宝付公钥路径
        String base64Str = RsaCodingUtil.decryptByPubCerFile(dataContent, cerPath);

        if (base64Str == null || base64Str.isEmpty()) {
            log.error("=====检查解密公钥是否正确！");
            throw new RuntimeException("检查解密公钥是否正确");
        }

        try {
            String dataStr = base64Decode(base64Str);
            log.info("=====返回查询数据解密结果:"+dataStr);
            Map<String, String> result = ConvertToXML.toMap(dataStr);
            return result;//JXMConvertUtil.JsonConvertHashMap(dataStr);
        } catch (IOException e) {
            log.error("base64 解码失败", e);
        }
        return null;
    }

    private String base64Encode(String str) throws UnsupportedEncodingException {
        return new BASE64Encoder().encode(str.getBytes("UTF-8"));
    }

    private String base64Decode(String str) throws IOException {
        return new String(new BASE64Decoder().decodeBuffer(str), "UTF-8");
    }

    /**
     * 解析参数中的键值对 ret_code=BF00121&ret_msg=报文交易要素格式错误:报文内容不能为空&data_content=
     *
     * @param strUrlParam
     * @return
     */
    private Map<String, String> responseValue(String strUrlParam) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            // 解析出键值
            if (arrSplitEqual.length > 1) {
                // 正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    // 只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;

    }






}
