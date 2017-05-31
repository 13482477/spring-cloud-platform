package com.siebre.payment.paymenthandler.basic.paymentrefund;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.mapper.serialnumber.SerialNumberMapper;
import com.siebre.payment.paymenthandler.basic.payment.PaymentInterfaceComponent;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entiry.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.service.paymenttransaction.PaymentTransactionService;

public abstract class AbstractPaymentRefundComponent implements PaymentInterfaceComponent<PaymentRefundRequest, PaymentRefundResponse> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @Autowired
    private SerialNumberMapper SerialNumberMapper;

    //abstract empty method implement PaymentInterface
    public PaymentRefundResponse handle(PaymentRefundRequest paymentRefundRequest){
        return null;
    }

    public PaymentRefundResponse handle(PaymentRefundRequest paymentRefundRequest, PaymentTransaction paymentTransaction, PaymentOrder paymentOrder, PaymentWay paymentWay, PaymentInterface paymentInterface){
        //获取原有交易的transaction {设置请求中的原始内部交易流水号，外部交易流水号}
        paymentRefundRequest.setOriginExternalNumber(paymentTransaction.getExternalTransactionNumber());
        paymentRefundRequest.setOriginInternalNumber(paymentTransaction.getInternalTransactionNumber());

        //TODO 查看是否存在已退款的transaction记录，判断该条记录是否是成功退款或者是处于失败退款的一个中间状态
        /*List<PaymentTransaction> refunds = paymentTransactionService.queryRefundTransaction(paymentOrder.getId());
        for (PaymentTransaction refund : refunds) {
            if(PaymentTransactionStatus.SUCCESS.equals(refund.getPaymentStatus())){
                logger.info("该订单已退款");

            }
        }*/

        PaymentTransaction refundPaymentTransaction = new PaymentTransaction();
        refundPaymentTransaction.setInterfaceType(PaymentInterfaceType.REFUND);
        refundPaymentTransaction.setPaymentStatus(PaymentTransactionStatus.PROCESSING);
        refundPaymentTransaction.setPaymentOrderId(paymentOrder.getId());
        refundPaymentTransaction.setPaymentWayId(paymentWay.getId());
        refundPaymentTransaction.setPaymentChannelId(paymentWay.getPaymentChannelId());
        //设置金额
        refundPaymentTransaction.setPaymentAmount(paymentRefundRequest.getRefundApplication().getRefundAmount());

        //设置退款交易的内部流水号，并同时作为退款申请的申请号存储的refundApplication
        refundPaymentTransaction.setInternalTransactionNumber(SerialNumberMapper.nextValue("refund_dep"));


        RefundApplication refundApplication = paymentRefundRequest.getRefundApplication();
        //更新状态为处理中
        refundApplication.setApplicationNumber(refundPaymentTransaction.getInternalTransactionNumber());
        refundApplication.setStatus(RefundApplicationStatus.PROCESSING);

        refundPaymentTransaction.setCreateDate(new Date());
        paymentTransactionService.createRefundPaymentTransaction(refundPaymentTransaction,refundApplication);
        paymentRefundRequest.setRefundTransaction(refundPaymentTransaction);

        PaymentRefundResponse paymentRefundResponse = this.handleInternal(paymentRefundRequest,refundPaymentTransaction,paymentOrder,paymentWay,paymentInterface);


        //同步状态下更新 退款application 退款transaction
        if(paymentRefundResponse.getSynchronize()){
            paymentTransactionService.synchronizedRefundConfirm(paymentRefundResponse.getRefundApplication(),paymentRefundResponse.getPaymentTransaction());
        }

        return paymentRefundResponse;
    }

    protected abstract PaymentRefundResponse handleInternal(PaymentRefundRequest paymentRefundRequest,PaymentTransaction paymentTransaction,PaymentOrder paymentOrder,PaymentWay paymentWay,PaymentInterface paymentInterface);
}
