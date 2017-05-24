package com.siebre.payment.service.paymenthandler.baofoo.pay.prepay;

import com.siebre.payment.service.paymenthandler.baofoo.pay.dto.BaofooRequest;
import com.siebre.payment.service.paymenthandler.baofoo.pay.dto.BaofooResponse;
import org.springframework.stereotype.Service;

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
