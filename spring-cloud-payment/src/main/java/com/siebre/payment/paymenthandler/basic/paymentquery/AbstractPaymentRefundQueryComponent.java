package com.siebre.payment.paymenthandler.basic.paymentquery;

import com.siebre.payment.paymenthandler.basic.payment.PaymentInterfaceComponent;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tianci.huang on 2017/7/12.
 */
public abstract class AbstractPaymentRefundQueryComponent implements PaymentInterfaceComponent<PaymentQueryRequest, PaymentQueryResponse> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(PaymentQueryRequest request, PaymentQueryResponse response) {
        logger.info("订单退款查询接口处理");
        this.handleInternal(request, response);
    }

    protected abstract void handleInternal(PaymentQueryRequest request, PaymentQueryResponse response);
}
