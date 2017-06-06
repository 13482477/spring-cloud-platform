package com.siebre.payment.paymenthandler.baofoo.pay.prepay;

import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooRequest;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooResponse;
import com.siebre.payment.paymenthandler.baofoo.sdk.BaofooApiClient;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 宝付快捷支付 确认支付
 */
@Component("baofooQuickPayPayment")
public class BaofooQuickPayPayment {

    private Logger logger = LoggerFactory.getLogger(BaofooQuickPayPayment.class);

    BaofooCon baofooCon = new BaofooCon();

    @Autowired
    private BaofooApiClient baofooApiClient;

    public BaofooResponse payment(BaofooRequest request, PaymentWay paymentWay) {
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("terminal_id", baofooCon.terminalId);// 终端号
        requestMap.put("member_id", paymentWay.getPaymentChannel().getMerchantCode());// 商户号
        requestMap.put("business_no", request.getBusinessCode());//预支付时返回的宝付业务流水号
        requestMap.put("sms_code", "");//持卡人收到的短信验证码
        requestMap.put("trans_serial_no", "KJAPI" + System.currentTimeMillis());//商户流水号

        return buildResponse(requestMap);
    }

    private BaofooResponse buildResponse(Map<String, Object> requestMap) {
        BaofooResponse response = new BaofooResponse();
        String request_url = "https://gw.baofoo.com/quickpay/api/confirmorder";//正式环境地址

        Map<String, String> result = baofooApiClient.send(requestMap, request_url, "pay");

        String stateResultStr = result.get("order_state_result");
        Map<String, String> stateResultMap = ConvertToXML.toMap(stateResultStr);
        //TODO 状态值待处理
        if (result.get("order_state_result") != null && result.get("order_state_result").equals("SUCCESS")) {//交易成功且业务成功
            response.setSuccess(true);
            response.setResponseCode("0000");
            response.setResponseMessage(result.get("resp_msg"));
            response.setSuccessAmount(result.get("succ_amt"));//成功金额
            if (result.get("version").equals(baofooCon.version) && requestMap.get("bind_id") == null) {//预支付无bind_Id且支付成功且版本号为“4.0.1.0”时返回
                response.setBindId(result.get("bind_id"));//绑定ID
            }
        } else {//失败
            response.setSuccess(false);
            response.setResponseCode(result.get("ret_code"));
            response.setResponseMessage(result.get("ret_msg"));
        }

        response.setExternalNumber(result.get("trans_no"));//宝付交易号
        return response;
    }


}
