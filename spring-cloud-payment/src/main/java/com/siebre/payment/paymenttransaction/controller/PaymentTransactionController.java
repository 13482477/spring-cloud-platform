package com.siebre.payment.paymenttransaction.controller;

import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import com.siebre.payment.paymenttransaction.vo.RefundRecord;
import com.siebre.payment.paymenttransaction.vo.TransactionVo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class PaymentTransactionController {

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @ApiOperation(value="订单详情-退款记录", notes = "订单详情-退款记录")
    @RequestMapping(value = "/api/v1/paymentTransactions/refund/{orderId}", method = GET)
    public WebResult<TransactionVo> loadRefundTransactions(@PathVariable Long orderId) {
        List<TransactionVo> results = new ArrayList<>();
        List<PaymentTransaction> refunds = paymentTransactionService.queryRefundTransaction(orderId);

        for (PaymentTransaction transactions: refunds) {
            TransactionVo vo = new TransactionVo();
            vo.setCreateDate(DateFormatUtils.format(transactions.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
            vo.setTransactionId(transactions.getId());
            vo.setInternalTransactionNumber(transactions.getInternalTransactionNumber());
            vo.setPaymentAmount(transactions.getPaymentAmount());
            vo.setPaymentStatus(transactions.getPaymentStatus().getDescription());
            results.add(vo);
        }
        TransactionVo result = new TransactionVo();
        if(results.size() > 0) {
            result = results.get(results.size() - 1);
        }

        return WebResult.<TransactionVo>builder().returnCode("200").data(result).returnMessage("调用成功").build();
    }

    @ApiOperation(value="退款详情-退款详情记录", notes = "退款详情-退款详情记录")
    @RequestMapping(value = "/api/v1/paymentTransactions/refundRecords/{refundApplicationId}", method = GET)
    public WebResult<List<RefundRecord>> getRefundRecords(@PathVariable Long refundApplicationId) {
        List<RefundRecord> list = paymentTransactionService.getRefundRecordFlow(refundApplicationId);

        return WebResult.<List<RefundRecord>>builder().returnCode("200").data(list).returnMessage("调用成功").build();
    }

}
