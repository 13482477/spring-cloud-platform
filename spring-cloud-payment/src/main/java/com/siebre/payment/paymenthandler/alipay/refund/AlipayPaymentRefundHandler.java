package com.siebre.payment.paymenthandler.alipay.refund;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenthandler.basic.paymentrefund.AbstractPaymentRefundComponent;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/24.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service("alipayPaymentRefundHandler")
public class AlipayPaymentRefundHandler extends AbstractPaymentRefundComponent {

    @Override
    protected void handleInternal(PaymentRefundRequest paymentRefundRequest, PaymentRefundResponse refundResponse, PaymentTransaction paymentTransaction,
                                                   PaymentOrder paymentOrder, PaymentWay paymentWay, PaymentInterface paymentInterface) {
        AlipayClient alipayClient = new DefaultAlipayClient(paymentInterface.getRequestUrl(),
                paymentWay.getAppId(), paymentWay.getSecretKey(), "json", AlipayConfig.INPUT_CHARSET_UTF,
                paymentWay.getPublicKey(), EncryptionMode.RSA.getDescription()); //获得初始化的AlipayClient

        AlipayTradeRefundRequest alipayRequest = buildAlipayRefundRequest(paymentRefundRequest, paymentWay, paymentTransaction);

        processRefund(paymentRefundRequest, refundResponse, alipayClient, alipayRequest);
    }

    /**
     * 构造请求参数
     *
     * @param paymentWay
     * @param paymentTransaction
     * @return
     */
    private AlipayTradeRefundRequest buildAlipayRefundRequest(PaymentRefundRequest paymentRefundRequest, PaymentWay paymentWay, PaymentTransaction paymentTransaction) {
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        alipayRequest.setBizContent(generateBizContent(paymentRefundRequest, paymentWay, paymentTransaction));

        return alipayRequest;
    }

    /**
     * 生成业务请求参数
     *
     * @return
     */
    private String generateBizContent(PaymentRefundRequest paymentRefundRequest, PaymentWay paymentWay, PaymentTransaction paymentTransaction) {
        Map<String, String> params = new HashMap<>();
        //订单支付时传入的商户订单号,不能和 trade_no同时为空
        params.put("out_trade_no", paymentRefundRequest.getOriginInternalNumber());
        //支付宝交易号，和商户订单号不能同时为空
        params.put("trade_no", paymentRefundRequest.getOriginExternalNumber());

        params.put("refund_amount", paymentTransaction.getPaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.put("refund_reason", paymentRefundRequest.getRefundApplication().getRequest());

        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
        params.put("out_request_no", paymentTransaction.getInternalTransactionNumber());

        return JsonUtil.mapToJson(params);
    }

    private void processRefund(PaymentRefundRequest paymentRefundRequest, PaymentRefundResponse refundResponse, AlipayClient alipayClient, AlipayTradeRefundRequest alipayRequest) {

        PaymentTransaction refundTransaction = paymentRefundRequest.getRefundTransaction();
        RefundApplication refundApplication = paymentRefundRequest.getRefundApplication();

        try {
            AlipayTradeRefundResponse response = alipayClient.execute(alipayRequest);
            refundResponse.setExternalTransactionNumber(response.getTradeNo());
            refundTransaction.setExternalTransactionNumber(response.getTradeNo());
            refundResponse.setReturnMessage(response.getMsg());
            if (response.isSuccess()) {
                refundTransaction.setPaymentStatus(PaymentTransactionStatus.SUCCESS);//退款交易调用成功
                refundApplication.setStatus(RefundApplicationStatus.SUCCESS);
                refundApplication.setResponse(RefundApplicationStatus.SUCCESS.getDescription());
                logger.info("调用成功");
            } else {
                refundTransaction.setPaymentStatus(PaymentTransactionStatus.FAILED);
                refundApplication.setStatus(RefundApplicationStatus.FAILED);
                refundApplication.setResponse(RefundApplicationStatus.FAILED.getDescription());
                logger.error("调用失败,失败原因={}",response.getMsg());
            }

        } catch (AlipayApiException e) {
            refundTransaction.setPaymentStatus(PaymentTransactionStatus.FAILED);
            refundApplication.setStatus(RefundApplicationStatus.FAILED);
            refundApplication.setResponse(RefundApplicationStatus.FAILED.getDescription());
            logger.error("支付宝退款接口调用异常", e);
        }

        refundResponse.setRefundApplication(refundApplication);
        refundResponse.setPaymentTransaction(refundTransaction);
    }
}
