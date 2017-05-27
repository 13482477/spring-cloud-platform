package com.siebre.payment.service.paymenthandler.basic.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entiry.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import com.siebre.payment.service.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.service.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.service.paymentorder.PaymentOrderOutOfTimeService;
import com.siebre.payment.service.paymenttransaction.PaymentTransactionService;
import com.siebre.payment.service.serialnumber.SerialNumberService;

/**
 * 支付组件
 *
 * @author lizhiqiang
 */
public abstract class AbstractPaymentComponent implements PaymentInterfaceComponent<PaymentRequest, PaymentResponse> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PaymentTransactionService paymentTransactionService;

    @Autowired
    protected PaymentWayService paymentWayService;

    @Autowired
    protected SerialNumberService serialNumberService;

    @Autowired
    protected PaymentOrderOutOfTimeService orderOutOfTimeService;

    @Autowired
    protected PaymentOrderMapper paymentOrderMapper;


    @Override
    public PaymentResponse handle(PaymentRequest request) {
        PaymentWay paymentWay = this.paymentWayService.getPaymentWayByCode(request.getPaymentWayCode()).getData();

        PaymentOrder paymentOrder = paymentOrderMapper.selectByOrderNumber(request.getOrderNumber());

        PaymentTransaction paymentTransaction = this.paymentTransactionService.createTransaction(paymentOrder, paymentWay);
        //订单状态改为支付中，并且更新订单渠道
        PaymentOrder orderForUpdate = new PaymentOrder();
        orderForUpdate.setId(paymentOrder.getId());
        orderForUpdate.setStatus(PaymentOrderPayStatus.PAYING);
        orderForUpdate.setPaymentChannelId(paymentWay.getPaymentChannelId());
        orderForUpdate.setPaymentWayCode(paymentWay.getCode());
        this.paymentOrderMapper.updateByPrimaryKeySelective(orderForUpdate);

        this.orderOutOfTimeService.newOrder(paymentOrder);//超时队列中记录新加入的订单

        return this.handleInternal(request, paymentWay, paymentOrder, paymentTransaction);
    }

    protected abstract PaymentResponse handleInternal(PaymentRequest request, PaymentWay paymentWay, PaymentOrder paymentOrder, PaymentTransaction paymentTransaction);

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
