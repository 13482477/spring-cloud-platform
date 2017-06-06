package com.siebre.payment.paymenthandler.baofoo.pay.prepay;

import com.siebre.basic.exception.SiebreRuntimeException;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooRequest;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooResponse;
import com.siebre.payment.paymenthandler.baofoo.sdk.BaofooApiClient;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 宝付快捷支付 预支付
 */
@Component("baofooQuickPayPrePay")
public class BaofooQuickPayPrePay{

    private Logger logger = LoggerFactory.getLogger(BaofooQuickPayPrePay.class);

    BaofooCon baofooCon = new BaofooCon();

    @Autowired
    private BaofooApiClient baofooApiClient;


    public BaofooResponse prePay(BaofooRequest request, PaymentTransaction paymentTransaction, PaymentOrder paymentOrder, PaymentWay paymentWay, PaymentInterface paymentInterface){

        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("terminal_id", baofooCon.terminalId);// 终端号
        requestMap.put("member_id", paymentWay.getPaymentChannel().getMerchantCode());// 商户号
        requestMap.put("acc_no", baofooCon.accNo);// 银行卡号
        requestMap.put("card_holder", baofooCon.cardHolder);// 持卡人姓名
        requestMap.put("id_card_type", baofooCon.idCardType);// 证件类型 01 身份证
        requestMap.put("id_card", baofooCon.idCard);// 证件号码
        requestMap.put("mobile", baofooCon.mobile);// 银行卡预留手机号

        //requestMap.put("valid_date", "");// 银行卡有效期,信用卡必输,格式 YYMM， 20 年 08 月格式为 2008
        //requestMap.put("cvv", "");// 银行卡安全码,信用卡必输，银行卡背后最后三位数字

        requestMap.put("user_id", baofooCon.userId);// 用户ID,用户在商户端的唯一标识ID
        requestMap.put("trans_id", paymentOrder.getOrderNumber());//商户订单号
        String amt = paymentTransaction.getPaymentAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        requestMap.put("txn_amt", amt);//交易金额,单位分
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(paymentTransaction.getCreateDate());
        requestMap.put("trade_date", date);//交易日期
        requestMap.put("trans_serial_no", "KJAPI" + System.currentTimeMillis());//商户流水号

        requestMap.put("risk_item", riskItemStr(date,requestMap,paymentOrder));//风控参数 必须json格式的字符串

        return buildResponse(requestMap,paymentInterface);
    }

    private BaofooResponse buildResponse(Map<String,Object> requestMap,PaymentInterface paymentInterface){
        BaofooResponse response = new BaofooResponse();
        //String request_url = "https://gw.baofoo.com/quickpay/api/prepareorder";//正式环境地址

        Map<String, String> result = baofooApiClient.send(requestMap,paymentInterface.getRequestUrl(),"prePay");

        if(result.get("resp_code") != null && result.get("resp_code").equals("0000")) {//交易成功且业务成功
            response.setSuccess(true);
            response.setResponseCode("0000");
            response.setResponseMessage(result.get("resp_msg"));
            response.setBusinessNumber(result.get("business_no"));//宝付业务流水号
            if(result.get("version").equals(baofooCon.version) && requestMap.get("bind_id") == null) {//预支付无bind_Id且支付成功且版本号为“4.0.1.0”时返回
                response.setBindId(result.get("bind_id"));//绑定ID
            }
        }else {//失败
            response.setSuccess(false);
            response.setResponseCode(result.get("ret_code"));
            response.setResponseMessage(result.get("ret_msg"));
        }

        return response;
    }

    /**
     * 风控参数
     * @param date
     * @param requestMap
     * @param paymentOrder
     * @return
     */
    private String riskItemStr(String date,Map<String, Object> requestMap,PaymentOrder paymentOrder){

        Map<String, String> dataMap = new HashMap<String, String>();
        //基本风控参数
        dataMap.put("goods_category",baofooCon.goodsCategory);//商品类目  保险类=2005
        dataMap.put("user_no",baofooCon.userId);//商户用户唯一标识
        dataMap.put("user_login_id",baofooCon.userloginId);//商户用户登录名
        dataMap.put("user_mobile",baofooCon.mobile);//绑定手机号
        dataMap.put("register_user_name",baofooCon.userloginId);//用户注册姓名
        dataMap.put("register_time",date);//注册时间
        try {
            dataMap.put("trans_ip", InetAddress.getLocalHost().getHostAddress());//持卡人支付IP
        } catch (Exception e) {
            logger.error("Get local ip address error!", e);
            throw new SiebreRuntimeException("Baofoo payment request get local ip address error!");
        }
        dataMap.put("identify_state",baofooCon.identifyState);//是否实名认证 1是 0不是
        dataMap.put("identify_type","");//实名认证方式 identity_state为1是必填，是实名认证时，必填 1：银行卡认证 2：现场认证 3：身份证远程认证 4：其它认证
        dataMap.put("device_no",paymentOrder.getOrderNumber());//设备指纹关联订单号

        //请求获取设备指纹
        Map<String, String> deviceIdRequest = new HashMap<String, String>();
        deviceIdRequest.put("member_id",requestMap.get("member_id").toString());
        String sessionId = requestMap.get("member_id").toString() + paymentOrder.getOrderNumber();
        deviceIdRequest.put("session_id",sessionId);//商户号+商户订单号,用来唯一标识设备指纹,memberId+transNo

        String deviceIdPost = baofooApiClient.doPost(deviceIdRequest,"https://fk.baofoo.com/getDeviceMem");
        Map<String, Object> deviceIdPostMap = JsonUtil.jsonToMap(deviceIdPost);
        logger.info("device_id_result_code = " + deviceIdPostMap.get("result_code") + "result_message = " + deviceIdPostMap.get("result_message"));
        String deviceId = "";
        if(deviceIdPostMap.get("result_code").equals("1")){//1 表示成功，非 1 则表示失败
            deviceId = deviceIdPostMap.get("device_id").toString();
        }
        dataMap.put("device_id",deviceId);//设备指纹

        //行业风控参数
        dataMap.put("applicant_name","张三");//投保人姓名
        dataMap.put("applicant_mobile",baofooCon.mobile);//投保人手机号

        String riskItem = JsonUtil.mapToJson(dataMap);//map转json
        logger.info("riskItem = " + riskItem);
        return riskItem;
    }


}
