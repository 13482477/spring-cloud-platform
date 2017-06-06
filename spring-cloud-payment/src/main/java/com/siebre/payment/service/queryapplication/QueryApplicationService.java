package com.siebre.payment.service.queryapplication;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentgateway.vo.PaymentOrderQueryRequest;
import com.siebre.payment.paymentgateway.vo.PaymentOrderQueryResponse;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentQueryComponent;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
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
public class QueryApplicationService{
    Logger logger = LoggerFactory.getLogger(QueryApplicationService.class);

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @Autowired
    private PaymentWayService paymentWayService;

    public ServiceResult<PaymentOrderQueryResponse> queryOrderStatusByOrderNumber(PaymentOrderQueryRequest request) throws Exception {
        PaymentTransaction transaction = paymentTransactionService.getPaymentTransactionForQuery(request.getOrderNumber());
        if(null == transaction){
            logger.error("查询失败，没有找到对应的有效交易orderNumber={}",request.getOrderNumber());
            return ServiceResult.<PaymentOrderQueryResponse>builder().success(false).message("没有找到对应的有效交易=" + request.getOrderNumber()).build();
        }
        //根据交易的transaction找到支付对应的paymentWay,再获得退款对应的paymentInterface
        PaymentWay paymentWay = paymentWayService.getPaymentWay(transaction.getPaymentWay().getCode());
        PaymentChannel channel = paymentWay.getPaymentChannel();
        PaymentInterface paymentInterface = paymentWayService.getPaymentInterface(paymentWay.getCode(), PaymentInterfaceType.QUERY);

        PaymentQueryRequest paymentQueryRequest = new PaymentQueryRequest();
        paymentQueryRequest.setPaymentTransaction(transaction);
        paymentQueryRequest.setOrderNumber(request.getOrderNumber());
        paymentQueryRequest.setExternalNumber(transaction.getExternalTransactionNumber());
        paymentQueryRequest.setInternalNumber(transaction.getInternalTransactionNumber());
        paymentQueryRequest.setPaymentChannel(channel);
        paymentQueryRequest.setPaymentWay(paymentWay);
        paymentQueryRequest.setPaymentInterface(paymentInterface);

        AbstractPaymentQueryComponent paymentComponent = (AbstractPaymentQueryComponent) SpringContextUtil.getBean(paymentInterface.getHandlerBeanName());
        PaymentQueryResponse paymentQueryResponse = paymentComponent.handle(paymentQueryRequest);

        PaymentOrderQueryResponse paymentOrderQueryResponse = new PaymentOrderQueryResponse();
        paymentOrderQueryResponse.setStatus(paymentQueryResponse.getStatus());

        return ServiceResult.<PaymentOrderQueryResponse>builder().success(true).data(paymentOrderQueryResponse).build();
    }
}
