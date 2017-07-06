package com.siebre.payment.paymenthandler.alipay.query;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentQueryComponent;
import com.siebre.payment.paymenthandler.paymentquery.OrderQueryReturnVo;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/26.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 开发文档地址：https://doc.open.alipay.com/doc2/apiDetail.htm?apiId=757&docType=4
 */
@Service("alipayPaymentQueryHandler")
public class AlipayPaymentQueryHandler extends AbstractPaymentQueryComponent {

    @Autowired
    private PaymentWayMapper paymentWayMapper;

    @Override
    protected void handleInternal(PaymentQueryRequest request, PaymentQueryResponse response) {
        //TODO 对于支付宝电脑网关支付，使用的是手机网关支付的paymentWay
        PaymentOrder order = response.getLocalOrder();
        PaymentWay paymentWay = request.getPaymentWay();
        if(paymentWay.getCode().equals(AlipayConfig.WAY_WEB_PAY)) {
            paymentWay = paymentWayMapper.getPaymentWayByCode(AlipayConfig.WAY_TRADE_PAY);
        }
        PaymentInterface paymentInterface = request.getPaymentInterface();

        AlipayClient alipayClient = new DefaultAlipayClient(paymentInterface.getRequestUrl(),
                paymentWay.getAppId(), paymentWay.getSecretKey(), "json", AlipayConfig.INPUT_CHARSET_UTF,
                paymentWay.getPublicKey(), EncryptionMode.RSA.getDescription()); //获得初始化的AlipayClient

        AlipayTradeQueryRequest alipayRequest = buildAlipayQueryRequest(order);

        processQuery(alipayClient, alipayRequest, response);

    }

    /**
     * 构造请求参数
     *
     * @return
     */
    private AlipayTradeQueryRequest buildAlipayQueryRequest(PaymentOrder order) {
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();//创建API对应的request
        alipayRequest.setBizContent(generateBizContent(order));
        return alipayRequest;
    }

    /**
     * 生成业务请求参数
     *
     * @return
     */
    private String generateBizContent(PaymentOrder order) {
        Map<String, String> params = new HashMap<>();

        params.put("out_trade_no", order.getOrderNumber());
        params.put("trade_no", order.getExternalOrderNumber());

        return JsonUtil.mapToJson(params);
    }


    private void processQuery(AlipayClient alipayClient, AlipayTradeQueryRequest alipayRequest, PaymentQueryResponse response) {
        try {
            PaymentOrder order = response.getLocalOrder();
            AlipayTradeQueryResponse alipayResponse = alipayClient.execute(alipayRequest);

            if (alipayResponse.isSuccess()) {
                logger.info("调用成功");
                OrderQueryReturnVo queryResult = new OrderQueryReturnVo();
                String status = alipayResponse.getTradeStatus();
                if ("WAIT_BUYER_PAY".equals(status)) { //交易创建，等待买家付款
                    queryResult.setTradeState(PaymentOrderPayStatus.PAYING);
                } else if ("TRADE_CLOSED".equals(status)) {  //未付款交易超时关闭，或支付完成后全额退款
                    if(PaymentOrderPayStatus.PAYING.equals(order.getStatus())) {
                        queryResult.setTradeState(PaymentOrderPayStatus.INVALID);
                    } else if (PaymentOrderPayStatus.INVALID.equals(order.getStatus())) {
                        queryResult.setTradeState(PaymentOrderPayStatus.INVALID);
                    } else if (PaymentOrderPayStatus.FULL_REFUND.equals(order.getStatus())) {
                        queryResult.setTradeState(PaymentOrderPayStatus.FULL_REFUND);
                    }
                } else if ("TRADE_SUCCESS".equals(status)) { //TRADE_SUCCESS
                    queryResult.setTradeState(PaymentOrderPayStatus.PAID);
                    queryResult.setRemoteOrderAmount(new BigDecimal(alipayResponse.getTotalAmount()));
                    queryResult.setRemotePayTime(alipayResponse.getSendPayDate());
                } else if ("TRADE_FINISHED".equals(status)) { //交易结束，不可退款
                    // https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7386797.0.0.pFtKIz&source=search&treeId=193&articleId=106448&docType=1
                    // TODO 待讨论
                }

                response.setReturnCode(ReturnCode.SUCCESS.getDescription());
                response.setQueryResult(queryResult);
            } else {
                logger.info("查询失败，失败原因：{}, {}", alipayResponse.getMsg(), alipayResponse.getSubMsg());
                response.setReturnCode(ReturnCode.FAIL.getDescription());
                response.setReturnMessage("查询失败，失败原因：" + alipayResponse.getMsg() + ", " + alipayResponse.getSubMsg());
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝查询接口调用异常", e);
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage("支付宝查询接口调用异常");
        }
    }

}
