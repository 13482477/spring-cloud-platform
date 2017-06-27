package com.siebre.payment.paymenthandler.alipay.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.entity.enums.SubsequentAction;
import com.siebre.payment.hostconfig.entity.PaymentHostConfig;
import com.siebre.payment.hostconfig.service.PaymentHostConfigService;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Huang Tianci
 *         支付宝--手机网站支付
 */
@Component("alipayTradeWapPaymentHandler")
public class AlipayTradeWapPaymentHandler extends AbstractPaymentComponent {

    @Autowired
    private PaymentHostConfigService hostConfig;

    @Override
    protected void handleInternal(PaymentRequest request, PaymentResponse response, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction) {

        PaymentOrder paymentOrder = request.getPaymentOrder();
        AlipayClient alipayClient = new DefaultAlipayClient(paymentInterface.getRequestUrl(),
                paymentWay.getAppId(), paymentWay.getSecretKey(), "json", AlipayConfig.INPUT_CHARSET_UTF,
                paymentWay.getPublicKey(), paymentWay.getEncryptionMode().getDescription()); //获得初始化的AlipayClient

        AlipayTradeWapPayRequest alipayRequest = buildAlipayTradeWapPayRequest(paymentWay, paymentInterface, paymentTransaction, paymentOrder);
        paymentTransaction.setRequestJsonStr(JsonUtil.toJson(alipayRequest, true));

        String form = "";
        try {
            form = alipayClient.sdkExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        response.setPayUrl(paymentInterface.getRequestUrl() + "?" + form);
        response.setReturnCode(ReturnCode.SUCCESS.getDescription());
        response.setReturnMessage("调用成功");
        response.setSubsequentAction(SubsequentAction.REDIRECT_TO_PAYMENT_GATEWAY.getValue());
    }

    /**
     * 构造请求参数
     *
     * @param paymentWay
     * @param paymentTransaction
     * @return
     */
    private AlipayTradeWapPayRequest buildAlipayTradeWapPayRequest(PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction, PaymentOrder paymentOrder) {
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(hostConfig.getFrontHost() + paymentInterface.getReturnPageUrl() + "?orderNumber=" + paymentOrder.getOrderNumber());
        alipayRequest.setNotifyUrl(hostConfig.getPaymentHost() + paymentInterface.getCallbackUrl());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent(generateBizContent(paymentWay, paymentTransaction, paymentOrder));
        return alipayRequest;
    }

    /**
     * 生成业务请求参数
     *
     * @return
     */
    private String generateBizContent(PaymentWay paymentWay, PaymentTransaction paymentTransaction, PaymentOrder paymentOrder) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("subject", "保险产品");
        params.put("out_trade_no", paymentTransaction.getInternalTransactionNumber());
        params.put("timeout_express", "30m"); //30分钟后没有付款，将关闭交易
        params.put("total_amount", paymentOrder.getAmount());
        params.put("seller_id", paymentWay.getPaymentChannel().getPayeeAccount());
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\"");
            sb.append(":");
            sb.append("\"").append(entry.getValue()).append("\"");
            sb.append(",");
        }
        //支付宝官方文档中是写成product_code,但是提供的sdk中写成prod_code
        sb.append("\"").append("prod_code").append("\"");
        sb.append(":");
        sb.append("\"").append("QUICK_WAP_PAY").append("\"");
        sb.append("}");
        return sb.toString();
    }

}
