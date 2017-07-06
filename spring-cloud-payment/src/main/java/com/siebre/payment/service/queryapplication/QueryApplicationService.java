package com.siebre.payment.service.queryapplication;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentQueryComponent;
import com.siebre.payment.paymenthandler.config.HandlerBeanNameConfig;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huang Tianci
 */
@Service
public class QueryApplicationService {
    Logger logger = LoggerFactory.getLogger(QueryApplicationService.class);

    @Autowired
    private PaymentWayService paymentWayService;

    @Autowired
    private PaymentOrderService orderService;

    /**
     * 去远程查询第三方支付系统中本地订单对应的支付状态
     */
    public PaymentQueryResponse queryOrderStatusByOrderNumber(String orderNumber) throws Exception {
        logger.info("开始去第三方渠道查询订单状态，订单编号：{}", orderNumber);
        PaymentQueryResponse response = new PaymentQueryResponse();
        PaymentOrder order = orderService.queryPaymentOrder(orderNumber);
        if (order == null) {
            logger.info("未在本地查询到该订单号对应的订单。订单号：{}", orderNumber);
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage("未在本地查询到该订单号对应的订单。订单号：" + orderNumber);
            return response;
        }
        response.setLocalOrder(order);
        if (PaymentOrderPayStatus.UNPAID.equals(order.getStatus())) {
            logger.info("订单状态为未支付，无法查询。订单号：{}", orderNumber);
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage("订单状态为未支付，无法查询");
            return response;
        }
        if (PaymentOrderPayStatus.INVALID.equals(order.getStatus())) {
            logger.info("该订单状态为已失效，无法查询。订单号：{}", orderNumber);
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage("该订单状态为已失效，无法查询");
            return response;
        }
        PaymentWay paymentWay = paymentWayService.getPaymentWay(order.getPaymentWayCode());
        PaymentChannel channel = paymentWay.getPaymentChannel();
        PaymentInterface paymentInterface = paymentWayService.getPaymentInterface(paymentWay.getCode(), PaymentInterfaceType.QUERY);

        PaymentQueryRequest request = new PaymentQueryRequest();
        request.setPaymentChannel(channel);
        request.setPaymentWay(paymentWay);
        request.setPaymentInterface(paymentInterface);

        String handlerBeanName = HandlerBeanNameConfig.QUERY_MAPPING.get(paymentWay.getCode());
        AbstractPaymentQueryComponent paymentComponent = (AbstractPaymentQueryComponent) SpringContextUtil.getBean(handlerBeanName);
        paymentComponent.handle(request, response);

        return response;
    }
}
