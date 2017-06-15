package com.siebre.payment.paymenthandler.basic.payment;

import com.siebre.payment.entity.enums.*;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentlistener.PaymentOrderOutOfTimeService;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.mapper.PaymentTransactionMapper;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import com.siebre.payment.serialnumber.service.SerialNumberService;
import com.siebre.payment.service.queryapplication.QueryApplicationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 支付组件
 *
 * @author lizhiqiang
 */
public abstract class AbstractPaymentComponent implements PaymentInterfaceComponent<PaymentRequest, PaymentResponse> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    protected PaymentTransactionService paymentTransactionService;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    protected PaymentWayService paymentWayService;

    @Autowired
    protected SerialNumberService serialNumberService;

    @Autowired
    protected PaymentOrderOutOfTimeService orderOutOfTimeService;

    @Autowired
    protected PaymentOrderMapper paymentOrderMapper;

    @Autowired
    private QueryApplicationService queryApplicationService;

    @Override
    public void handle(PaymentRequest request, PaymentResponse response) {
        PaymentOrder paymentOrder = request.getPaymentOrder();
        response.setPaymentOrder(paymentOrder);

        PaymentWay paymentWay = this.paymentWayService.getPaymentWayByCode(request.getPaymentWayCode()).getData();

        //检查锁定
        if (PaymentOrderLockStatus.LOCK.equals(paymentOrder.getLockStatus())) {
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage("支付失败，订单已被锁定");
            response.setPaymentOrder(paymentOrder);
            return;
        }
        //检查是否是未支付状态
        if (!PaymentOrderPayStatus.UNPAID.equals(paymentOrder.getStatus())) {
            response.setReturnCode(ReturnCode.FAIL.getDescription());
            response.setReturnMessage("支付失败, 该订单状态为" + paymentOrder.getStatus().getDescription());
            response.setPaymentOrder(paymentOrder);
            return;
        }

        PaymentTransaction paymentTransaction = this.paymentTransactionService.createTransaction(paymentOrder, paymentWay);
        //订单状态改为支付中，并且更新订单渠道
        this.paymentOrderService.updateOrderStatus(paymentOrder, PaymentOrderPayStatus.PAYING);

        this.orderOutOfTimeService.newOrder(paymentOrder);//超时队列中记录新加入的订单

        PaymentInterface paymentInterface = getPayTypeInterface(paymentWay);

        this.handleInternal(request, response, paymentWay, paymentInterface, paymentTransaction);

        if (ReturnCode.FAIL.getDescription().equals(response.getReturnCode())) {
            this.paymentOrderService.updateOrderStatus(paymentOrder, PaymentOrderPayStatus.PAYERROR);
        }
    }

    private PaymentInterface getPayTypeInterface(PaymentWay paymentWay) {
        for (PaymentInterface paymentInterface : paymentWay.getPaymentInterfaces()) {
            if (PaymentInterfaceType.PAY.equals(paymentInterface.getPaymentInterfaceType())) {
                return paymentInterface;
            }
        }
        return null;
    }

    protected abstract void handleInternal(PaymentRequest request, PaymentResponse response, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction);

    protected String getPaymentUrl(PaymentWay paymentWay, Map<String, String> params) {
        String url = this.getPaymentGateWayUrl(paymentWay);
        url = this.adjustUrl(url);
        String queryString = this.generateQueryString(params);

        String result = url + queryString;

        logger.info("payment url generated, url={}", result);

        return result;
    }

    protected String generateQueryString(Map<String, String> params) {
        List<String> keyValues = new ArrayList<String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            keyValues.add(entry.getKey() + "=" + entry.getValue());
        }
        return StringUtils.join(keyValues.iterator(), "&");
    }

    protected String getPaymentGateWayUrl(PaymentWay paymentWay) {
        for (PaymentInterface paymentInterface : paymentWay.getPaymentInterfaces()) {
            if (PaymentInterfaceType.PAY.equals(paymentInterface.getPaymentInterfaceType())) {
                if (StringUtils.isNotBlank(paymentInterface.getRequestUrl())) {
                    return paymentInterface.getRequestUrl();
                }
            }
        }
        return null;
    }

    protected String adjustUrl(String url) {
        String result = url;
        if (StringUtils.endsWith(result, "/")) {
            result = result.substring(0, result.length() - 2);
        }
        if (!StringUtils.endsWith(result, "?")) {
            result += "?";
        }
        return result;
    }
}
