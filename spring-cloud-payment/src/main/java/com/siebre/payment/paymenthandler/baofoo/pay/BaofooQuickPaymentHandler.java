package com.siebre.payment.paymenthandler.baofoo.pay;

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
    protected PaymentResponse handleInternal(PaymentRequest request, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentOrder paymentOrder, PaymentTransaction paymentTransaction) {

        BaofooRequest boofooRequest = new BaofooRequest();
        boofooRequest.setInternalNumber(paymentTransaction.getInternalTransactionNumber());

        BindCard bindCard = new BindCard();
        bindCard.setCardNumber(request.getAccountNo());

        boofooRequest.setBindCard(bindCard);

        BaofooResponse response = baofooQuickPayPrePay.prePay(boofooRequest,paymentTransaction,paymentOrder,paymentWay,paymentInterface);//预支付
        boofooRequest.setBusinessCode(response.getBusinessNumber());//宝付业务流水号

        Map<String, Object> result = new HashMap<String, Object>();
        if(response.getSuccess()){//预支付成功
            response = baofooQuickPayPayment.payment(boofooRequest,paymentWay);//支付
            String externalTransactionNumber = response.getExternalNumber();//外部交易号
            String seller_id = paymentWay.getPaymentChannel().getMerchantCode();//商户号
            if(response.getSuccess()){//支付成功
                BigDecimal total_fee = new BigDecimal(response.getSuccessAmount());
                this.paymentTransactionService.paymentConfirm(paymentTransaction.getInternalTransactionNumber(), externalTransactionNumber, seller_id, total_fee);
                result.put("transaction_result","success");
            }else {
                this.paymentTransactionService.setFailStatus(paymentTransaction.getInternalTransactionNumber(),externalTransactionNumber);
                result.put("transaction_result","fail");
            }
        }else {
            result.put("transaction_result","fail");
        }

        //TODO

        return null; //PaymentResponse.builder().body(result).build();
        //return PaymentResponse.builder().payUrl(paymentInterface.getRequestUrl()).body(response).build();
    }
}

