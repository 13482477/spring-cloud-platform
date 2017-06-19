package com.siebre.payment.paymenttransaction.controller;

import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class PaymentTransactionController {

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @ApiOperation(value="订单详情-退款记录列表", notes = "订单详情-退款记录列表")
    @RequestMapping(value = "/api/v1/paymentTransactions/refund/{orderId}", method = GET)
    public WebResult<List<PaymentTransaction>> loadRefundTransactions(@PathVariable Long orderId) {

        List<PaymentTransaction> refunds = paymentTransactionService.queryRefundTransaction(orderId);

        return WebResult.<List<PaymentTransaction>>builder().returnCode("200").data(refunds).returnMessage("调用成功").build();
    }

}
