package com.siebre.payment.service.queryapplication;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentRefundQueryComponent;
import com.siebre.payment.paymenthandler.config.HandlerBeanNameConfig;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
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
public class RefundQueryApplicationService {

    Logger logger = LoggerFactory.getLogger(RefundQueryApplicationService.class);

    @Autowired
    private PaymentWayService paymentWayService;

    @Autowired
    private PaymentOrderMapper orderMapper;

    /** 去第三方查询本地退款订单的状态 */
    public PaymentQueryResponse queryOrderRefundStatusByOrderNumber(String orderNumber) {
        logger.info("交易退款查询，订单编号：{}", orderNumber);
        PaymentQueryResponse response = new PaymentQueryResponse();
        PaymentOrder order = orderMapper.selectByOrderNumber(orderNumber);
        if (order == null) {
            logger.info("未在本地查询到该订单号对应的订单。订单号：{}", orderNumber);
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage("未在本地查询到该订单号对应的订单。订单号：" + orderNumber);
            return response;
        }
        response.setLocalOrder(order);
        if (PaymentOrderPayStatus.UNPAID.equals(order.getStatus()) ||
                PaymentOrderPayStatus.PAYING.equals(order.getStatus()) ||
                PaymentOrderPayStatus.PAID.equals(order.getStatus()) ||
                PaymentOrderPayStatus.INVALID.equals(order.getStatus()) ||
                PaymentOrderPayStatus.PAYERROR.equals(order.getStatus()) ||
                PaymentOrderPayStatus.REFUNDING.equals(order.getStatus())) {
            logger.info("订单状态为{}，无法查询。订单号：{}", order.getStatus().getDescription(), orderNumber);
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage("订单状态为" + order.getStatus().getDescription() + "，无法查询");
            return response;
        }

        PaymentWay paymentWay = paymentWayService.getPaymentWay(order.getPaymentWayCode());
        PaymentChannel channel = paymentWay.getPaymentChannel();
        PaymentInterface paymentInterface = paymentWayService.getPaymentInterface(paymentWay.getCode(), PaymentInterfaceType.REFUND_QUERY);

        PaymentQueryRequest request = new PaymentQueryRequest();
        request.setPaymentChannel(channel);
        request.setPaymentWay(paymentWay);
        request.setPaymentInterface(paymentInterface);

        String handlerBeanName = HandlerBeanNameConfig.REFUND_QUERY_MAPPING.get(paymentWay.getCode());
        AbstractPaymentRefundQueryComponent paymentComponent = (AbstractPaymentRefundQueryComponent) SpringContextUtil.getBean(handlerBeanName);
        paymentComponent.handle(request, response);

        return response;
    }

}
