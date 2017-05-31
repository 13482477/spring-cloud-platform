package com.siebre.payment.paymenthandler.alipay.refund;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.siebre.payment.paymentorder.entiry.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;

/**
 * @author Huang Tianci
 *         阿里快捷支付退款接口处理类
 */
@Service("alipayFastpayRefundHandler")
public class AlipayFastpayRefundHandler extends AbstractPaymentRefundComponent {

    @Autowired
    PaymentWayMapper paymentWayMapper;

    @Override
    protected PaymentRefundResponse handleInternal(PaymentRefundRequest paymentRefundRequest, PaymentTransaction paymentTransaction, PaymentOrder paymentOrder, PaymentWay paymentWay, PaymentInterface paymentInterface) {
        //TODO 待优化  即时到账退款接口使用的是手机网关退款接口
        paymentWay = paymentWayMapper.getPaymentWayByCode("ALIPAY_TRADE_WAP_PAY");
        AlipayClient alipayClient = new DefaultAlipayClient(paymentWay.getPaymentGatewayUrl(),
                paymentWay.getAppId(), paymentWay.getSecretKey(), "json", AlipayConfig.input_charset_utf,
                paymentWay.getPublicKey(), EncryptionMode.RSA.getDescription()); //获得初始化的AlipayClient

        AlipayTradeRefundRequest alipayRequest = buildAlipayRefundRequest(paymentRefundRequest, paymentWay, paymentTransaction);

        return processRefund(paymentRefundRequest, alipayClient, alipayRequest);
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

    private PaymentRefundResponse processRefund(PaymentRefundRequest paymentRefundRequest, AlipayClient alipayClient, AlipayTradeRefundRequest alipayRequest) {
        PaymentRefundResponse refundResponse = new PaymentRefundResponse();
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
                logger.info("调用成功");
            } else {
                refundTransaction.setPaymentStatus(PaymentTransactionStatus.FAILED);
                refundApplication.setStatus(RefundApplicationStatus.FAILED);
                logger.info("调用失败");
            }

        } catch (AlipayApiException e) {
            refundTransaction.setPaymentStatus(PaymentTransactionStatus.FAILED);
            refundApplication.setStatus(RefundApplicationStatus.FAILED);
            logger.error("支付宝退款接口调用异常", e);
        }

        refundResponse.setRefundApplication(refundApplication);
        refundResponse.setPaymentTransaction(refundTransaction);

        return refundResponse;
    }

    /**
     * 以下是即时到账有密批量退款接口
     */
    /*@Override
    protected PaymentRefundResponse handleInternal(PaymentRefundRequest paymentRefundRequest, PaymentTransaction paymentTransaction, PaymentOrder paymentOrder, PaymentWay paymentWay, PaymentInterface paymentInterface) {
        Map<String,String> params = this.generateParams(paymentRefundRequest,paymentWay,paymentTransaction,paymentInterface);
        this.processSign(params, paymentWay.getEncryptionMode(),paymentWay.getSecretKey());
        String result = this.refund(paymentInterface.getRequestUrl(),params);
        PaymentRefundResponse refundResponse = new PaymentRefundResponse();
        refundResponse.setReturnMessage(result);
        return refundResponse;
    }

    private String refund(String requestUrl, Map<String, String> params) {
        String result = HttpTookit.doPost(requestUrl,params);
        return result;
    }

    private Map<String, String> generateParams(PaymentRefundRequest paymentRefundRequest,PaymentWay paymentWay,PaymentTransaction paymentTransaction,PaymentInterface paymentInterface) {
        Map<String, String> params = new HashMap<>();
        params.put("service","refund_fastpay_by_platform_pwd");
        params.put("partner", paymentWay.getPaymentChannel().getMerchantCode());
        params.put("_input_charset", AlipayConfig.input_charset_utf);
        params.put("sign_type", paymentWay.getEncryptionMode().getDescription());
        params.put("notify_url",paymentInterface.getCallbackUrl());
        params.put("seller_email", "zhangqing@siebresystems.com");
        //params.put("seller_user_id", paymentWay.getPaymentChannel().getMerchantCode());
        params.put("refund_date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //退款批次号,格式:退款日期（8位）+流水号（3～24位）
        params.put("batch_no", paymentTransaction.getInternalTransactionNumber().substring(2,paymentTransaction.getInternalTransactionNumber().length() - 1));
        params.put("batch_num", "1");
        //交易退款数据集的格式为：原付款支付宝交易号^退款总金额^退款理由
        //退款理由
        params.put("detail_data", paymentRefundRequest.getOriginInternalNumber() + "^" + paymentTransaction.getPaymentAmount() + "^" + paymentRefundRequest.getRefundApplication().getRequest());
        return params;
    }

    private void processSign(Map<String, String> params, EncryptionMode encryptionMode, String secretKey) {
        if (EncryptionMode.MD5.equals(encryptionMode)) {
            String sign = AlipaySign.createMd5SignOrder(params, secretKey);
            logger.info("Alipay sign key generate, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
            params.put("sign", sign);
            return;
        } else if (EncryptionMode.RSA.equals(encryptionMode)) {
            String sign = AlipaySign.createRsaSignOrder(params, secretKey);
            logger.info("Alipay sign key generate, original paramerers={},encryptionMode={}, secretKey={},sign={}", params.toString(), encryptionMode.getDescription(), secretKey, sign);
            params.put("sign", sign);
            return;
        }
    }*/

}
