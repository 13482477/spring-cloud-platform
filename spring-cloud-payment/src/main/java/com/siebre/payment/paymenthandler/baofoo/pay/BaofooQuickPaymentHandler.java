package com.siebre.payment.paymenthandler.baofoo.pay;

import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooRequest;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BaofooResponse;
import com.siebre.payment.paymenthandler.baofoo.pay.dto.BindCard;
import com.siebre.payment.paymenthandler.baofoo.pay.prepay.BaofooQuickPayPayment;
import com.siebre.payment.paymenthandler.baofoo.pay.prepay.BaofooQuickPayPrePay;
import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Component("baofooQuickPaymentHandler")
public class BaofooQuickPaymentHandler extends AbstractPaymentComponent {

    private Logger log = LoggerFactory.getLogger(BaofooQuickPaymentHandler.class);

    @Autowired
    private BaofooQuickPayPrePay baofooQuickPayPrePay;

    @Autowired
    private BaofooQuickPayPayment baofooQuickPayPayment;

    @Override
    protected void handleInternal(PaymentRequest request, PaymentResponse response, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction) {
        PaymentOrder paymentOrder = request.getPaymentOrder();
        BaofooRequest boofooRequest = new BaofooRequest();
        boofooRequest.setInternalNumber(paymentTransaction.getInternalTransactionNumber());

        BindCard bindCard = new BindCard();
        bindCard.setCardNumber(request.getAccountNo());

        boofooRequest.setBindCard(bindCard);

        BaofooResponse baofooResponse = baofooQuickPayPrePay.prePay(boofooRequest, paymentTransaction, paymentOrder, paymentWay, paymentInterface);//预支付

        String responseStr = JsonUtil.toJson(baofooResponse, true);

        boofooRequest.setBusinessCode(baofooResponse.getBusinessNumber());//宝付业务流水号

        Map<String, Object> result = new HashMap<String, Object>();
        if (baofooResponse.getSuccess()) {//预支付成功
            baofooResponse = baofooQuickPayPayment.payment(boofooRequest, paymentWay);//支付
            String externalTransactionNumber = baofooResponse.getExternalNumber();//外部交易号
            String seller_id = paymentWay.getPaymentChannel().getMerchantCode();//商户号
            if (baofooResponse.getSuccess()) {//支付成功
                BigDecimal total_fee = new BigDecimal(baofooResponse.getSuccessAmount());
                //TODO 需要从回调中取出支付成功时间
                this.paymentTransactionService.paymentConfirm(paymentTransaction.getInternalTransactionNumber(), externalTransactionNumber, seller_id, total_fee, new Date(), responseStr);
                result.put("transaction_result", "success");
            } else {
                this.paymentTransactionService.setFailStatus(paymentTransaction.getInternalTransactionNumber(), externalTransactionNumber);
                result.put("transaction_result", "fail");
            }
        } else {
            result.put("transaction_result", "fail");
        }

        //TODO

        //PaymentResponse.builder().body(result).build();
        //return PaymentResponse.builder().payUrl(paymentInterface.getRequestUrl()).body(response).build();
    }
}

