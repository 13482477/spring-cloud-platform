package com.siebre.payment.paymenthandler.baofoo.pay.prepay;

import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooRequest;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooResponse;
import com.siebre.payment.paymenthandler.baofoo.sdk.BaofooApiClient;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/20.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 宝付快捷支付 预绑卡
 */
@Component("baofooQuickPreBind")
public class BaofooQuickPreBind {

    @Autowired
    private BaofooApiClient baofooApiClient;

    public BaofooResponse preBind(BaofooRequest request, PaymentTransaction paymentTransaction){
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("terminal_id", "34424");// 终端号
        requestMap.put("member_id", "1160889");// 商户号
        //requestMap.put("additional_info", "附加字段");// 附加字段
        //requestMap.put("req_reserved", "请求方保留域");// 请求方保留域
        requestMap.put("trans_serial_no", "KJAPI" + System.currentTimeMillis());// 商户流水号

        //requestMap.put("acc_no", request.getBindCard().getCardNumber());// 银行卡号
        requestMap.put("acc_no", "6225211503906389");// 银行卡号
        requestMap.put("card_type", "101");// 卡类型 借记卡 101,信用卡 102
        requestMap.put("card_holder", "蒋美兰");// 持卡人姓名
        requestMap.put("id_card_type", "01");// 证件类型 01 身份证
        requestMap.put("id_card", "320481199211085223");// 证件号码
        requestMap.put("mobile", "15501520762");// 银行卡预留手机号
        //requestMap.put("valid_date", "");// 银行卡有效期,信用卡必输,格式 YYMM， 20 年 08 月格式为 2008
        //requestMap.put("cvv", "");// 银行卡安全码,信用卡必输，银行卡背后最后三位数字
        requestMap.put("user_id", "123456789");// 用户ID,用户在商户端的唯一标识ID
        requestMap.put("trade_date", new SimpleDateFormat("yyyyMMddHHmmss").format(paymentTransaction.getCreateDate()));// 交易日期


        return buildResponse(requestMap);
    }

    private BaofooResponse buildResponse(Map<String,Object> requestMap){
        BaofooResponse response = new BaofooResponse();
        //String request_url = "https://vgw.baofoo.com/quickpay/api/preparebind";//测试环境地址
        String request_url = "https://gw.baofoo.com/quickpay/api/preparebind";//正式环境地址

        Map<String, String> result = baofooApiClient.send(requestMap,request_url,"fastPay");

        if(result.get("resp_code") != null && result.get("resp_code").equals("0000")){//交易成功且业务成功
            response.setSuccess(true);
            response.setResponseCode("0000");
            response.setResponseMessage(result.get("resp_msg"));
            response.setPreBindCode(result.get("unique_code"));//预绑卡唯一码
        }
        if(result.get("ret_code") != null){//交易失败
            response.setSuccess(false);
            response.setResponseCode(result.get("ret_code"));
            response.setResponseMessage(result.get("ret_msg"));
        }

        //response.setBusinessNumber(result.get("trans_serial_no"));
        return response;
    }
}

