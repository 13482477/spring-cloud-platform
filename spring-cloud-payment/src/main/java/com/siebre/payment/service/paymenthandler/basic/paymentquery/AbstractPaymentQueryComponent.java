package com.siebre.payment.service.paymenthandler.basic.paymentquery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.entity.paymenttransaction.PaymentTransaction;
import com.siebre.payment.service.paymenthandler.basic.payment.PaymentInterfaceComponent;
import com.siebre.payment.service.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.service.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.service.paymentorder.PaymentOrderService;
import com.siebre.payment.service.paymenttransaction.PaymentTransactionService;
import com.siebre.payment.service.paymentway.PaymentWayService;

public abstract class AbstractPaymentQueryComponent implements PaymentInterfaceComponent<PaymentQueryRequest, PaymentQueryResponse> {
    protected Logger logger  = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PaymentOrderService orderService;

    @Autowired
    private PaymentTransactionService transactionService;

    @Autowired
    private PaymentWayService paymentWayService;

    public PaymentQueryResponse handle(PaymentQueryRequest request){
        logger.info("订单查询接口处理");
        PaymentQueryResponse response  = this.handleInternal(request);

        this.processOrderStatus(request,response);
        return response;
    }

    protected abstract PaymentQueryResponse handleInternal(PaymentQueryRequest request);


    private void processOrderStatus(PaymentQueryRequest request,PaymentQueryResponse response){
        PaymentTransaction transaction = request.getPaymentTransaction();
        PaymentOrder order = transaction.getPaymentOrder();
        //TODO 查询结果处理订单进行更新
        PaymentOrderPayStatus orderPayStatus = bindingPayStatus(response.getStatus());
        transactionService.updateTransactionAndOrderStatus(transaction.getId(), response.getStatus(),order.getId(),orderPayStatus);
    }

    private PaymentOrderPayStatus bindingPayStatus(PaymentTransactionStatus paymentTransactionStatus){
        if(PaymentTransactionStatus.SUCCESS.equals(paymentTransactionStatus)){
            return PaymentOrderPayStatus.PAID;
        }else if(PaymentTransactionStatus.FAILED.equals(paymentTransactionStatus)){
            return PaymentOrderPayStatus.PAYERROR;
        }else if(PaymentTransactionStatus.PROCESSING.equals(paymentTransactionStatus)){
            return PaymentOrderPayStatus.PAYING;
        }else if(PaymentTransactionStatus.CLOSED.equals(paymentTransactionStatus)){
            return PaymentOrderPayStatus.CLOSED;
        }
        return null;
    }

}
