package com.siebre.payment.paymenthandler.alipay.query;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentQueryComponent;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentRefundQueryComponent;
import com.siebre.payment.paymenthandler.paymentquery.OrderQueryReturnVo;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.refundapplication.mapper.RefundApplicationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Huang Tianci
 * 退款查询
 */
@Service("alipayPaymentRefundQueryHandler")
public class AlipayPaymentRefundQueryHandler extends AbstractPaymentRefundQueryComponent {

    @Autowired
    private PaymentWayMapper paymentWayMapper;

    @Autowired
    private RefundApplicationMapper refundApplicationMapper;

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

        AlipayTradeFastpayRefundQueryRequest refundQueryRequest = buildAlipayQueryRequest(order);

        processQuery(alipayClient, refundQueryRequest, response);

    }

    /**
     * 构造请求参数
     *
     * @return
     */
    private AlipayTradeFastpayRefundQueryRequest buildAlipayQueryRequest(PaymentOrder order) {
        AlipayTradeFastpayRefundQueryRequest refundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();//创建API对应的request
        refundQueryRequest.setBizContent(generateBizContent(order));
        return refundQueryRequest;
    }

    /**
     * 生成业务请求参数
     *
     * @return
     */
    private String generateBizContent(PaymentOrder order) {
        Map<String, String> params = new HashMap<>();

        RefundApplication refundApplication = refundApplicationMapper.selectByBusinessNumber(order.getOrderNumber(), null);

        params.put("out_trade_no", order.getOrderNumber());
        params.put("trade_no", order.getExternalOrderNumber());
        params.put("out_request_no", refundApplication.getRefundApplicationNumber());

        return JsonUtil.mapToJson(params);
    }


    private void processQuery(AlipayClient alipayClient, AlipayTradeFastpayRefundQueryRequest refundQueryRequest, PaymentQueryResponse response) {
        try {
            PaymentOrder order = response.getLocalOrder();
            AlipayTradeFastpayRefundQueryResponse refundQueryResponse = alipayClient.execute(refundQueryRequest);
            response.setRemoteJson(JsonUtil.toJson(refundQueryResponse, true));
            if (refundQueryResponse.isSuccess()) {
                logger.info("调用成功");
                OrderQueryReturnVo queryResult = new OrderQueryReturnVo();
                response.setReturnCode(ReturnCode.SUCCESS.getDescription());

                String refundAmountStr = refundQueryResponse.getRefundAmount();
                if(StringUtils.isBlank(refundAmountStr)) {
                    //没有查到数据，代表未退款成功或者未退款
                    if (PaymentOrderPayStatus.PROCESSING_REFUND.equals(order.getStatus())) {
                        queryResult.setTradeState(PaymentOrderPayStatus.REFUNDERROR);
                    } else {
                        queryResult.setTradeState(order.getStatus());
                        response.setReturnCode(ReturnCode.FAIL.getDescription());
                        response.setReturnMessage("未查询到订单退款信息");
                    }
                } else {
                    BigDecimal refundAmount = new BigDecimal(refundAmountStr);
                    BigDecimal totalAmount = new BigDecimal(refundQueryResponse.getTotalAmount());
                    if(refundAmount.compareTo(totalAmount) == 0) {
                        queryResult.setTradeState(PaymentOrderPayStatus.FULL_REFUND);
                    } else {
                        queryResult.setTradeState(PaymentOrderPayStatus.PART_REFUND);
                    }
                    queryResult.setRemoteOrderAmount(totalAmount);
                    queryResult.setRemoteOrderRefundAmount(refundAmount);
                }
                response.setQueryResult(queryResult);
            } else {
                logger.info("查询失败，失败原因：{}, {}", refundQueryResponse.getMsg(), refundQueryResponse.getSubMsg());
                response.setReturnCode(ReturnCode.FAIL.getDescription());
                response.setReturnMessage("查询失败，失败原因：" + refundQueryResponse.getMsg() + ", " + refundQueryResponse.getSubMsg());
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝退款查询接口调用异常", e);
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage("支付宝退款查询接口调用异常");
        }
    }

}
