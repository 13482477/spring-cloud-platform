package com.siebre.payment.paymenthandler.basic.paymentrefund;

import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.paymenthandler.basic.payment.PaymentInterfaceComponent;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.serialnumber.mapper.SerialNumberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract class AbstractPaymentRefundComponent implements PaymentInterfaceComponent<PaymentRefundRequest, PaymentRefundResponse> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @Autowired
    private SerialNumberMapper SerialNumberMapper;

    @Override
    public void handle(PaymentRefundRequest paymentRefundRequest, PaymentRefundResponse paymentRefundResponse) {
        PaymentOrder paymentOrder = paymentRefundRequest.getPaymentOrder();
        PaymentWay paymentWay = paymentRefundRequest.getPaymentWay();

        //获取原有交易的transaction {设置请求中的原始内部交易流水号，外部交易流水号}
        paymentRefundRequest.setOriginExternalNumber(paymentOrder.getExternalOrderNumber());
        paymentRefundRequest.setOriginInternalNumber(paymentOrder.getOrderNumber());

        //TODO 是否去数据库中查找refundPaymentTransaction
        PaymentTransaction refundPaymentTransaction = new PaymentTransaction();
        refundPaymentTransaction.setInterfaceType(PaymentInterfaceType.REFUND);
        refundPaymentTransaction.setPaymentStatus(PaymentTransactionStatus.REFUND_PROCESSING);
        refundPaymentTransaction.setPaymentOrderId(paymentOrder.getId());
        refundPaymentTransaction.setPaymentWayId(paymentWay.getId());
        refundPaymentTransaction.setPaymentChannelId(paymentWay.getPaymentChannelId());
        //设置金额
        refundPaymentTransaction.setPaymentAmount(paymentRefundRequest.getRefundApplication().getRefundAmount());

        //设置退款交易的内部流水号，并同时作为退款申请的申请号存储的refundApplication
        refundPaymentTransaction.setInternalTransactionNumber(SerialNumberMapper.nextValue("refund_dep"));

        RefundApplication refundApplication = paymentRefundRequest.getRefundApplication();
        //更新状态为处理中
        refundApplication.setRefundApplicationNumber(refundPaymentTransaction.getInternalTransactionNumber());
        refundApplication.setStatus(RefundApplicationStatus.PROCESSING);

        refundPaymentTransaction.setCreateDate(new Date());
        //保存refundPaymentTransaction和refundApplication
        paymentTransactionService.createRefundPaymentTransaction(refundPaymentTransaction, refundApplication);

        paymentRefundRequest.setRefundTransaction(refundPaymentTransaction);

        this.handleInternal(paymentRefundRequest, paymentRefundResponse);


        //同步状态下更新 退款application 退款transaction
        if (paymentRefundResponse.getSynchronize()) {
            paymentTransactionService.synchronizedRefundConfirm(paymentOrder, paymentRefundResponse.getRefundApplication(), paymentRefundResponse.getRefundTransaction());
        }

    }

    protected abstract void handleInternal(PaymentRefundRequest paymentRefundRequest, PaymentRefundResponse paymentRefundResponse);
}
