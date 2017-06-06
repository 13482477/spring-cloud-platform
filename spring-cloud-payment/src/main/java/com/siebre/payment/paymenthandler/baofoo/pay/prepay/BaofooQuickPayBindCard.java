package com.siebre.payment.paymenthandler.baofoo.pay.prepay;

import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooRequest;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooResponse;
import com.siebre.payment.paymenthandler.baofoo.sdk.BaofooApiClient;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 宝付快捷支付 绑定卡
 *
 */
public class BaofooQuickPayBindCard {
    @Autowired
    private BaofooApiClient baofooApiClient;


    public BaofooResponse bindCard(BaofooRequest request, BaofooResponse response, PaymentTransaction paymentTransaction){

        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("terminal_id", "34424");// 终端号
        requestMap.put("member_id", "1160889");// 商户号
        requestMap.put("sms_code","");//短信验证码
        requestMap.put("unique_code",response.getPreBindCode());//预绑卡唯一码
        requestMap.put("trade_date", new SimpleDateFormat("yyyyMMddHHmmss").format(paymentTransaction.getCreateDate()));// 交易日期
        requestMap.put("trans_serial_no", "KJAPI" + System.currentTimeMillis());// 商户流水号

        return buildResponse(requestMap);
    }

    private BaofooResponse buildResponse(Map<String,Object> requestMap){
        BaofooResponse response = new BaofooResponse();
        String request_url = "https://gw.baofoo.com/quickpay/api/bindcard";//正式环境地址

        Map<String, String> result = baofooApiClient.send(requestMap,request_url,"fastPay");


        return response;
    }

}

