package com.siebre.payment.paymenthandler.wechatpay.query;

import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentQueryComponent;
import com.siebre.payment.paymenthandler.paymentquery.OrderQueryReturnVo;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymenthandler.wechatpay.sdk.WeChatParamConvert;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.utils.JsonUtils;
import com.siebre.payment.utils.http.HttpTookit;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.JsonNode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Huang Tianci
 *         开发文档地址：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2
 */
@Service("weChatQueryHandler")
public class WeChatQueryHandler extends AbstractPaymentQueryComponent {

    @Override
    protected void handleInternal(PaymentQueryRequest request, PaymentQueryResponse response) {
        PaymentOrder order = response.getLocalOrder();
        PaymentInterface paymentInterface = request.getPaymentInterface();
        PaymentWay paymentWay = request.getPaymentWay();
        Map<String, String> params = generateQueryParams(paymentWay, order);
        this.processSign(params, paymentWay.getEncryptionMode(), paymentWay.getSecretKey());
        String requestUrl = paymentInterface.getRequestUrl();
        query(params, requestUrl, response);
    }

    private void query(Map<String, String> params, String requestUrl, PaymentQueryResponse response) {
        String queryXml = ConvertToXML.toXml(params);
        String resultXml = HttpTookit.doPost(requestUrl, queryXml);
        Map<String, String> resultMap = ConvertToXML.toMap(resultXml);
        response.setRemoteResult(resultMap);
        if ("SUCCESS".equals(resultMap.get("return_code"))) {
            OrderQueryReturnVo queryResult = new OrderQueryReturnVo();
            String trade_state = resultMap.get("trade_state");
            if ("SUCCESS".equals(trade_state) || "REFUND".equals(trade_state)) {
                queryResult.setTradeState(PaymentOrderPayStatus.PAID);
                //单位为分,转为元
                queryResult.setRemoteOrderAmount(new BigDecimal(resultMap.get("total_fee")).divide(new BigDecimal(100)));
                DateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
                try {
                    queryResult.setRemotePayTime(f.parse(resultMap.get("time_end")));
                } catch (ParseException e) {
                    logger.info("时间转换失败", e);
                }
            } else if ("NOTPAY".equals(trade_state) || "USERPAYING".equals(trade_state)) {
                queryResult.setTradeState(PaymentOrderPayStatus.PAYING);
            } else if ("PAYERROR".equals(trade_state)) {
                queryResult.setTradeState(PaymentOrderPayStatus.PAYERROR);
            } else if ("CLOSED".equals(trade_state)) {
                queryResult.setTradeState(PaymentOrderPayStatus.INVALID);
            }

            response.setQueryResult(queryResult);
            response.setReturnCode(ReturnCode.SUCCESS.getDescription());
        } else {
            logger.error("查询失败，失败原因={}", params.get("return_msg"));
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage(params.get("return_msg"));
        }
    }

    private Map<String, String> generateQueryParams(PaymentWay paymentWay, PaymentOrder order) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", paymentWay.getAppId());
        params.put("mch_id", paymentWay.getPaymentChannel().getMerchantCode());
        if (StringUtils.isNotBlank(order.getExternalOrderNumber())) {
            params.put("transaction_id", order.getExternalOrderNumber());
        } else {
            params.put("out_trade_no", order.getOrderNumber());
        }
        params.put("nonce_str", String.valueOf(UUID.randomUUID()).substring(0, 31));
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
