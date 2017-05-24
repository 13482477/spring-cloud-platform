package com.siebre.payment.service.paymenthandler.baofoo.pay.prepay;

import com.siebre.payment.service.paymenthandler.baofoo.pay.dto.BaofooRequest;
import com.siebre.payment.service.paymenthandler.baofoo.pay.dto.BaofooResponse;
import com.siebre.payment.service.paymenthandler.baofoo.sdk.BaofooApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 宝付快捷支付 绑定卡
 *
 */
public class BaofooQuickPayBindCard {

    private BaofooApiClient client;


    public void bindCard(BaofooRequest request,BaofooResponse response){

    }

    private BaofooResponse buildResponse(Map<String,String> responseMap){
        BaofooResponse response = new BaofooResponse();

        return response;
    }

}
