package com.siebre.payment.paymenthandler.basic.paymentquery;

import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymenthandler.basic.payment.PaymentInterfaceComponent;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractPaymentQueryComponent implements PaymentInterfaceComponent<PaymentQueryRequest, PaymentQueryResponse> {
    protected Logger logger  = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(PaymentQueryRequest request, PaymentQueryResponse response){
        logger.info("订单查询接口处理");
        this.handleInternal(request, response);
    }

    protected abstract void handleInternal(PaymentQueryRequest request, PaymentQueryResponse response);

}
