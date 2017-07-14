package com.siebre.payment.paymenthandler.wechatpay.query;

import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentRefundQueryComponent;
import com.siebre.payment.paymenthandler.paymentquery.OrderQueryReturnVo;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymenthandler.wechatpay.sdk.WeChatParamConvert;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.utils.http.HttpTookit;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Huang Tianci
 *         开发文档地址：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_5
 */
@Service("weChatRefundQueryHandler")
public class WeChatRefundQueryHandler extends AbstractPaymentRefundQueryComponent {

    @Override
    protected void handleInternal(PaymentQueryRequest request, PaymentQueryResponse response) {
        PaymentOrder order = response.getLocalOrder();
        PaymentInterface paymentInterface = request.getPaymentInterface();
        PaymentWay paymentWay = request.getPaymentWay();
        Map<String, String> params = generateQueryParams(paymentWay, order);
        this.processSign(params, paymentWay.getEncryptionMode(), paymentWay.getSecretKey());
        String requestUrl = paymentInterface.getRequestUrl();
        queryRefund(params, requestUrl, response);
    }

    private void queryRefund(Map<String, String> params, String requestUrl, PaymentQueryResponse response) {
        PaymentOrder order = response.getLocalOrder();

        String queryXml = ConvertToXML.toXml(params);
        String resultXml = HttpTookit.doPost(requestUrl, queryXml);
        Map<String, String> resultMap = ConvertToXML.toMap(resultXml);
        response.setRemoteJson(JsonUtil.toJson(resultMap, true));
        if ("SUCCESS".equals(resultMap.get("return_code"))) {
            String result_code = resultMap.get("result_code");
            if ("SUCCESS".equals(result_code)) {
                OrderQueryReturnVo queryResult = new OrderQueryReturnVo();
                int account = Integer.valueOf(resultMap.get("refund_count"));
                String refund_status = "refund_status_" + (account - 1);
                refund_status = resultMap.get(refund_status);
                if("SUCCESS".equals(refund_status)) {
                    //退款成功
                    BigDecimal refundTotalAmount = new BigDecimal(resultMap.get("refund_fee_" + (account - 1)));
                    refundTotalAmount = refundTotalAmount.divide(new BigDecimal(100));
                    if(order.getAmount().compareTo(refundTotalAmount) == 0) {
                        queryResult.setTradeState(PaymentOrderPayStatus.FULL_REFUND);
                    } else {
                        queryResult.setTradeState(PaymentOrderPayStatus.PART_REFUND);
                    }
                    queryResult.setRemoteOrderRefundAmount(refundTotalAmount);
                } else if("PROCESSING".equals(refund_status)) {
                    //退款处理中
                    queryResult.setTradeState(PaymentOrderPayStatus.PROCESSING_REFUND);
                } else if("CHANGE".equals(refund_status)) {
                    //退款异常对应退款失败
                    queryResult.setTradeState(PaymentOrderPayStatus.REFUNDERROR);
                }
                //TODO REFUNDCLOSE  退款关闭状态对应我们哪一个状态

                response.setQueryResult(queryResult);
                response.setReturnCode(ReturnCode.SUCCESS.getDescription());
            } else {
                logger.error("退款查询失败，失败原因={}，{}", params.get("err_code"), params.get("err_code_des"));
                response.setReturnCode(ReturnCode.FAIL.getDescription());
                response.setReturnMessage(params.get("err_code") + "," + params.get("err_code_des"));
            }
        } else {
            logger.error("退款查询失败，失败原因={}", params.get("return_msg"));
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage(params.get("return_msg"));
        }
    }

    private Map<String, String> generateQueryParams(PaymentWay paymentWay, PaymentOrder order) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", paymentWay.getAppId());
        params.put("mch_id", paymentWay.getPaymentChannel().getMerchantCode());
        params.put("nonce_str", String.valueOf(UUID.randomUUID()).substring(0, 31));
        if (StringUtils.isNotBlank(order.getExternalOrderNumber())) {
            params.put("transaction_id", order.getExternalOrderNumber());
        } else {
            params.put("out_trade_no", order.getOrderNumber());
        }

        params.put("sign_type", paymentWay.getEncryptionMode().getDescription());
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
