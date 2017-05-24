package com.siebre.payment.service.paymenthandler.wechatpay.query;

import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.paymentinterface.PaymentInterface;
import com.siebre.payment.entity.paymentway.PaymentWay;
import com.siebre.payment.service.paymenthandler.basic.paymentquery.AbstractPaymentQueryComponent;
import com.siebre.payment.service.paymenthandler.wechatpay.sdk.WeChatParamConvert;
import com.siebre.payment.serviceinterface.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.serviceinterface.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.utils.http.HttpTookit;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Huang Tianci
 */
@Service("weChatQueryHandler")
public class WeChatQueryHandler extends AbstractPaymentQueryComponent {


    /**
     * 调用微信查询订单状态
     * 1.需要微信查询的paymentWay
     * 2.需要微信查询的interface,请求地址从interface中获取
     * @param request
     * @return
     */
    @Override
    protected PaymentQueryResponse handleInternal(PaymentQueryRequest request) {
        PaymentInterface paymentInterface = request.getPaymentInterface();
        PaymentWay paymentWay = request.getPaymentWay();
        Map<String,String> params = generateQueryParams(paymentWay,paymentInterface,request);
        this.processSign(params, paymentWay.getEncryptionMode(), paymentWay.getSecretKey());
        PaymentTransactionStatus status = getPaymentStatus(params, paymentInterface);
        PaymentQueryResponse response = new PaymentQueryResponse();
        response.setStatus(status);
        return response;
    }

    private PaymentTransactionStatus getPaymentStatus(Map<String, String> params, PaymentInterface paymentInterface) {
        PaymentTransactionStatus status = null;
        String queryXml = ConvertToXML.toXml(params);

        String resultXml = HttpTookit.doPost(paymentInterface.getRequestUrl(), queryXml);
        Map<String, String> resultMap = ConvertToXML.toMap(resultXml);

        if("SUCCESS".equals(resultMap.get("return_code"))){
            switch (resultMap.get("trade_state")){
                case "SUCCESS":
                    status = PaymentTransactionStatus.SUCCESS;
                    break;
                case "REFUND"://转入退款，在我们系统中说明交易已关闭
                case "CLOSED":
                    status = PaymentTransactionStatus.CLOSED;
                    break;
                case "NOTPAY"://未支付对应我们的支付中
                case "USERPAYING":
                    status = PaymentTransactionStatus.PROCESSING;
                    break;
                case "PAYERROR":
                    status = PaymentTransactionStatus.FAILED;
                    break;
            }
        }else{
            logger.error("查询失败，失败原因={}",params.get("return_msg"));
        }
        return status;
    }


    private Map<String, String> generateQueryParams(PaymentWay paymentWay, PaymentInterface paymentInterface,PaymentQueryRequest request) {
        Map<String,String> params = new HashMap<>();
        params.put("appid",paymentWay.getAppId());
        params.put("mch_id",paymentWay.getPaymentChannel().getMerchantCode());
        if(StringUtils.isNotBlank(request.getExternalNumber())){
            params.put("transaction_id",request.getExternalNumber());
        }else{
            params.put("out_trade_no",request.getInternalNumber());
        }
        params.put("nonce_str",String.valueOf(UUID.randomUUID()).substring(0, 31));
        params.put("sign_type",paymentWay.getEncryptionMode().getDescription());
        return params;
    }

    private void processSign(Map<String, String> params, EncryptionMode encryptionMode, String secretKey) {
        if (EncryptionMode.MD5.equals(encryptionMode)) {
            String sign = WeChatParamConvert.signMd5(params, secretKey);
            logger.info("Wechat sign key generated, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
            params.put("sign", sign);
            return;
        }
    }

}
