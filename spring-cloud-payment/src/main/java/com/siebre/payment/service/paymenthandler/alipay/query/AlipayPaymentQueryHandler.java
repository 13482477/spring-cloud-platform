package com.siebre.payment.service.paymenthandler.alipay.query;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.paymenttransaction.PaymentTransaction;
import com.siebre.payment.entity.paymentway.PaymentWay;
import com.siebre.payment.service.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.service.paymenthandler.basic.paymentquery.AbstractPaymentQueryComponent;
import com.siebre.payment.service.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.service.paymenthandler.paymentquery.PaymentQueryResponse;

/**
 * Created by AdamTang on 2017/4/26.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service("alipayPaymentQueryHandler")
public class AlipayPaymentQueryHandler extends AbstractPaymentQueryComponent {
    @Override
    protected PaymentQueryResponse handleInternal(PaymentQueryRequest request) {
        PaymentWay paymentWay = request.getPaymentWay();
        PaymentTransaction paymentTransaction = request.getPaymentTransaction();

        AlipayClient alipayClient = new DefaultAlipayClient(paymentWay.getPaymentGatewayUrl(),
                paymentWay.getAppId(), paymentWay.getSecretKey(), "json", AlipayConfig.input_charset_utf,
                paymentWay.getPublicKey(),  EncryptionMode.RSA.getDescription()); //获得初始化的AlipayClient

        AlipayTradeQueryRequest alipayRequest = buildAlipayQueryRequest(paymentWay,paymentTransaction);

        return processQuery(alipayClient,alipayRequest);

    }

    /**
     * 构造请求参数
     * @param paymentWay
     * @param paymentTransaction
     * @return
     */
    private AlipayTradeQueryRequest buildAlipayQueryRequest(PaymentWay paymentWay, PaymentTransaction paymentTransaction) {
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();//创建API对应的request
        alipayRequest.setBizContent(generateBizContent(paymentWay,paymentTransaction));
        return alipayRequest;
    }

    /**
     * 生成业务请求参数
     * @return
     */
    private String generateBizContent(PaymentWay paymentWay, PaymentTransaction paymentTransaction) {
        Map<String, String> params = new HashMap<>();

        params.put("out_trade_no", paymentTransaction.getInternalTransactionNumber());
        params.put("trade_no", paymentTransaction.getExternalTransactionNumber());

        return JsonUtil.mapToJson(params);
    }



    private PaymentQueryResponse processQuery(AlipayClient alipayClient,AlipayTradeQueryRequest alipayRequest){
        PaymentQueryResponse queryResponse = new PaymentQueryResponse();

        try {

            AlipayTradeQueryResponse response =  alipayClient.execute(alipayRequest) ;

            if(response.isSuccess()){
                String status = response.getTradeStatus();
                //支付成功
                if("TRADE_SUCCESS".equals(status)){
                    queryResponse.setStatus(PaymentTransactionStatus.SUCCESS);
                }else if("WAIT_BUYER_PAY".equals(status)){//等待支付
                    queryResponse.setStatus(PaymentTransactionStatus.PROCESSING);
                }else if("TRADE_FINISHED".equals(status)){//支付关闭
                    queryResponse.setStatus(PaymentTransactionStatus.CLOSED);
                }else if ("TRADE_CLOSED".equals(status)){//支付失败（未付款交易超时关闭，或支付完成后全额退款
                    queryResponse.setStatus(PaymentTransactionStatus.FAILED);
                }

                logger.info("调用成功");
            } else {

                logger.info("调用失败");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝查询接口调用异常",e);
        }

        return queryResponse;
    }

}
