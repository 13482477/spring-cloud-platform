package com.siebre.payment.paymenthandler.baofoo.pay.prepay;

import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooRequest;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooResponse;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BindCard;
import com.siebre.payment.paymenthandler.baofoo.sdk.BaofooApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 宝付快捷支付 查询绑定卡
 */
public class BaofooQuickPayQueryBind{

    private BaofooApiClient client;


    private BaofooResponse queryBindCard(BaofooRequest request){
        Map<String,String> requestParams = new HashMap<>();

        requestParams.put("user_id",request.getUserID());//用户 ID

        Map<String,Object> responseMap = client.send(requestParams,request.getRequestUrl());


        return buildResponse(responseMap);
    }


    private BaofooResponse buildResponse(Map<String,Object> responseMap){
        if(responseMap == null){
            return null;
        }

        BaofooResponse response = new BaofooResponse();

        response.setResponseCode((String) responseMap.get("resp_code"));
        response.setResponseMessage((String)responseMap.get("resp_msg"));

        //交易失败不进行解析直接返回
        if(!response.isSuccess()){
            return response;
        }

        List<Map<String,Object>> cardList= (List<Map<String,Object>>) responseMap.get("card_list");

        for(Map<String,Object> cardMap:cardList){
            BindCard pojo = new BindCard();

            pojo.setBindId((String)cardMap.get("bind_id"));
            pojo.setBankCode((String)cardMap.get("bank_code"));
            pojo.setBankName((String)cardMap.get("bind_name"));
            pojo.setCardNumber((String)cardMap.get("card_no"));
            pojo.setCardType((String)cardMap.get("card_type"));

            response.addCardList(pojo);
        }

        return response;

    }

}
