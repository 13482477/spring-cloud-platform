package com.siebre.payment.paymenthandler.basic.payment;

import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentlistener.PaymentOrderOutOfTimeService;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
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
    public PaymentResponse handle(PaymentRequest request) {
        PaymentWay paymentWay = this.paymentWayService.getPaymentWayByCode(request.getPaymentWayCode()).getData();

        PaymentOrder paymentOrder = paymentOrderMapper.selectByOrderNumber(request.getOrderNumber());

        /**
         * TODO 检查order状态，为未支付状态可以直接支付
         */

        //TODO 先简单处理，如果已存在支付中的订单,直接更新为交易关闭，5月12号之后再处理
        PaymentTransaction transactionForCheck = paymentTransactionService.getPaymentTransactionForQuery(paymentOrder.getOrderNumber());
        if(transactionForCheck != null) {
            if(PaymentTransactionStatus.PROCESSING.equals(transactionForCheck.getPaymentStatus())){
                PaymentTransaction paymentTransactionForUpdate = new PaymentTransaction();
                paymentTransactionForUpdate.setId(transactionForCheck.getId());
                paymentTransactionForUpdate.setPaymentStatus(PaymentTransactionStatus.CLOSED);
                this.paymentTransactionMapper.updateByPrimaryKeySelective(paymentTransactionForUpdate);
            }
        }

        PaymentTransaction paymentTransaction = this.paymentTransactionService.createTransaction(paymentOrder, paymentWay);
        //订单状态改为支付中，并且更新订单渠道
        PaymentOrder orderForUpdate = new PaymentOrder();
        orderForUpdate.setId(paymentOrder.getId());
        orderForUpdate.setStatus(PaymentOrderPayStatus.PAYING);
        orderForUpdate.setPaymentChannelId(paymentWay.getPaymentChannelId());
        orderForUpdate.setPaymentWayCode(paymentWay.getCode());
        this.paymentOrderMapper.updateByPrimaryKeySelective(orderForUpdate);

        this.orderOutOfTimeService.newOrder(paymentOrder);//超时队列中记录新加入的订单

        PaymentInterface paymentInterface = getPayTypeInterface(paymentWay);

        return this.handleInternal(request, paymentWay, paymentInterface, paymentOrder, paymentTransaction);
    }

    private PaymentInterface getPayTypeInterface(PaymentWay paymentWay) {
        for(PaymentInterface paymentInterface : paymentWay.getPaymentInterfaces()) {
            if(PaymentInterfaceType.PAY.equals(paymentInterface.getPaymentInterfaceType())){
                return paymentInterface;
            }
        }
        return null;
    }

    protected abstract PaymentResponse handleInternal(PaymentRequest request, PaymentWay paymentWay,PaymentInterface paymentInterface, PaymentOrder paymentOrder, PaymentTransaction paymentTransaction);

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
