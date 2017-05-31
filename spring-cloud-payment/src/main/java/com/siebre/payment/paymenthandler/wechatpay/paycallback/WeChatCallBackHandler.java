package com.siebre.payment.paymenthandler.wechatpay.paycallback;

import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.paymenthandler.basic.paymentcallback.AbstractPaymentCallBackHandler;
import com.siebre.payment.paymenthandler.wechatpay.sdk.WeChatParamConvert;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/17.
 * 微信扫码支付回调
 */
@Component("weChatCallBackHandler")
public class WeChatCallBackHandler extends AbstractPaymentCallBackHandler {

    @Override
    protected Object callBackHandleInternal(HttpServletRequest request, HttpServletResponse response, PaymentInterface paymentInterface) {
        try {
            InputStream inputStream = request.getInputStream();

            byte[] bytes = this.readBytes(inputStream, request.getContentLength());
            String xml = new String(bytes);
            Map<String, String> map = ConvertToXML.toMap(xml);
            PaymentWay paymentWay = paymentInterface.getPaymentWay();
            if (validateSign(map, paymentWay)) {

                logger.info("微信签名验证成功");

                String internalTransactionNumber = map.get("out_trade_no");
                String externalTransactionNumber = map.get("transaction_id");
                String mch_id = map.get("mch_id");
                BigDecimal total_fee = new BigDecimal(map.get("total_fee")).divide(new BigDecimal("100"));
                this.paymentTransactionService.paymentConfirm(internalTransactionNumber, externalTransactionNumber, mch_id, total_fee);
                return "";
            }else{
                logger.info("微信签名验证失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private boolean validateSign(Map<String, String> map, PaymentWay paymentWay) {
        if (StringUtils.isBlank(map.get("sign"))) {
            return false;
        }

        HashMap<String, String> filterMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if ("sign".equals(entry.getKey())) {
                continue;
            }
            if (StringUtils.isNotBlank(entry.getValue())) {
                filterMap.put(entry.getKey(), entry.getValue());
            }
        }

        String signCode = null;

        if (EncryptionMode.MD5.equals(paymentWay.getEncryptionMode())) {
            signCode = WeChatParamConvert.signMd5(filterMap, paymentWay.getSecretKey());
        }

        return StringUtils.equals(signCode, map.get("sign"));
    }


    private byte[] readBytes(InputStream is, int contentLen) {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {// Should not happen.
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                // Ignore
                // e.printStackTrace();
            }
        }
        return new byte[]{};
    }
}
