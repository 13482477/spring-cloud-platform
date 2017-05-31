package com.siebre.payment.paymenthandler.baofoo.pay.prepay;

import org.springframework.stereotype.Service;

import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooRequest;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooResponse;

import java.util.Map;

/**
 * Created by AdamTang on 2017/4/20.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 宝付快捷支付 预绑卡
 */
public class BaofooQuickPreBind {

    public BaofooResponse preBind(BaofooRequest request){
        return null;
    }

    private BaofooResponse buildResponse(Map<String,String> responseMap){
        BaofooResponse response = new BaofooResponse();


        return response;
    }
}
