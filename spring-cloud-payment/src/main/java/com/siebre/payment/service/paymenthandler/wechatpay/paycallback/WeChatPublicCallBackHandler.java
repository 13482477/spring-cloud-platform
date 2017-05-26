package com.siebre.payment.service.paymenthandler.wechatpay.paycallback;

import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.paymentinterface.PaymentInterface;
import com.siebre.payment.entity.paymentway.PaymentWay;
import com.siebre.payment.service.paymenthandler.basic.paymentcallback.AbstractPaymentCallBackHandler;
import com.siebre.payment.service.paymenthandler.wechatpay.sdk.WeChatParamConvert;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Huang Tianci
 * 微信公众号支付回调接口
 */
@Service("weChatPublicCallBackHandler")
public class WeChatPublicCallBackHandler extends AbstractPaymentCallBackHandler {

    @Override
    protected Object callBackHandleInternal(HttpServletRequest request, HttpServletResponse response, PaymentInterface paymentInterface) {
        Map<String,String> responseMap = new HashMap<>();
        responseMap.put("return_code","<![CDATA[SUCCESS]]>");
        responseMap.put("return_msg","<![CDATA[]]>");
        try {
            InputStream inputStream = request.getInputStream();

            byte[] bytes = this.readBytes(inputStream, request.getContentLength());
            String xml = new String(bytes);
            Map<String, String> map = ConvertToXML.toMap(xml);
            if("SUCCESS".equals(map.get("return_code"))){
                PaymentWay paymentWay = paymentInterface.getPaymentWay();
                if (validateSign(map, paymentWay)) {

                    logger.info("微信签名验证成功");
                    if("SUCCESS".equals(map.get("result_code"))){
                        String internalTransactionNumber = map.get("out_trade_no");
                        String externalTransactionNumber = map.get("transaction_id");
                        String mch_id = map.get("mch_id");
                        BigDecimal total_fee = new BigDecimal(map.get("total_fee")).divide(new BigDecimal("100"));
                        this.paymentTransactionService.paymentConfirm(internalTransactionNumber, externalTransactionNumber, mch_id, total_fee);
                    } else {
                        logger.info("交易失败，请检查原因={}",map.get("return_msg"));
                        responseMap.put("return_msg","<![CDATA[交易失败]]>");
                    }
                }else{
                    logger.info("微信签名验证失败");
                    responseMap.put("return_msg","<![CDATA[微信签名验证失败]]>");
                }
            } else {
                responseMap.put("return_msg","<![CDATA[交易失败]]>");
            }
            String reString = ConvertToXML.toXml(responseMap);
            response.getWriter().println(reString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ConvertToXML.toXml(responseMap);
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
