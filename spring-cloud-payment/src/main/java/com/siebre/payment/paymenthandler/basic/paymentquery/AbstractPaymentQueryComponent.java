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

    @Autowired
    private PaymentOrderService orderService;

    @Autowired
    private PaymentTransactionService transactionService;

    @Autowired
    private PaymentWayService paymentWayService;

    @Override
    public void handle(PaymentQueryRequest request, PaymentQueryResponse response){
        logger.info("订单查询接口处理");
        this.handleInternal(request, response);

        this.processOrderStatus(request,response);
    }

    protected abstract void handleInternal(PaymentQueryRequest request, PaymentQueryResponse response);


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
            return PaymentOrderPayStatus.INVALID;
        }
        return null;
    }

}
