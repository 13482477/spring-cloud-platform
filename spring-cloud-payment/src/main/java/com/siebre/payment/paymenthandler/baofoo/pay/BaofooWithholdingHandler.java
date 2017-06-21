package com.siebre.payment.paymenthandler.baofoo.pay;

import com.siebre.payment.paymenthandler.baofoo.pay.prepay.BaofooCon;
import com.siebre.payment.paymenthandler.baofoo.sdk.BaofooApiClient;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by meilan on 2017/5/22.
 * 宝付代扣支付
 */
@Component("baofooWithholdingHandler")
public class BaofooWithholdingHandler extends AbstractPaymentComponent {

    private Logger logger = LoggerFactory.getLogger(BaofooWithholdingHandler.class);

    BaofooCon baofooCon = new BaofooCon();

    @Autowired
    private BaofooApiClient baofooApiClient;

    @Override
    protected void handleInternal(PaymentRequest request, PaymentResponse response, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction) {

        PaymentOrder paymentOrder = request.getPaymentOrder();
        //String request_url = "https://public.baofoo.com/cutpayment/api/backTransRequest";//正式环境地址

        Map<String, Object> requestDate = requestParams(paymentTransaction,paymentWay,paymentOrder);

        Map<String, String> resultMap = baofooApiClient.send(requestDate,paymentInterface.getRequestUrl(),"pay");
        Map<String, Object> result = new HashMap<String, Object>();

        String seller_id = resultMap.get("member_id");
        BigDecimal total_fee = new BigDecimal(resultMap.get("succ_amt"));
        String externalTransactionNumber = resultMap.get("trans_no");
        if(resultMap.get("resp_code ") != null && resultMap.get("resp_code ").equals("0000")) {//交易成功且业务成功
            //TODO 支付成功时间从回调中获取
            this.paymentTransactionService.paymentConfirm(paymentTransaction.getInternalTransactionNumber(), externalTransactionNumber, seller_id, total_fee, new Date());

            result.put("transaction_result","success");
        }else {//失败
            paymentTransactionService.setFailStatus(paymentTransaction.getInternalTransactionNumber(),externalTransactionNumber);

            result.put("transaction_result","fail");
        }

        //TODO
        // PaymentResponse.builder().body(result).build();
    }

    private Map<String, Object> requestParams(PaymentTransaction paymentTransaction,PaymentWay paymentWay,PaymentOrder paymentOrder){
        Map<String, Object> requestMap = new HashMap<String, Object>();

        requestMap.put("txn_sub_type", baofooCon.txnSubType);//交易子类
        requestMap.put("biz_type", baofooCon.bizType);//接入类型  其他：不填写和默认 0000,表示为储蓄卡支付。
        requestMap.put("terminal_id", baofooCon.terminalId);//终端号
        requestMap.put("member_id", paymentWay.getPaymentChannel().getMerchantCode());//商户号
        requestMap.put("trans_serial_no", "TISN"+System.currentTimeMillis());//商户流水号
        requestMap.put("trade_date", new SimpleDateFormat("yyyyMMddHHmmss").format(paymentTransaction.getCreateDate()));
        //requestMap.put("additional_info", "附加信息");//附加字段
        //requestMap.put("req_reserved", "保留");//请求方保留域

        requestMap.put("pay_code", baofooCon.payCode);//银行编码
        requestMap.put("pay_cm", baofooCon.payCm);//安全标识 默认为：2 1:不进行信息严格验证 2:对四要素（身份证号、持卡人姓名、银行卡绑定手机、卡号）进行严格校验
        requestMap.put("id_card_type", baofooCon.idCardType);//身份证类型
        requestMap.put("acc_no", baofooCon.accNo);//卡号
        requestMap.put("id_card", baofooCon.idCard);//身份证号
        requestMap.put("id_holder", baofooCon.idHolder);//持卡人姓名
        requestMap.put("mobile", baofooCon.mobile);
        //requestMap.put("valid_date", "");//卡有效期
        //requestMap.put("valid_no", "");//卡安全码
        requestMap.put("trans_id", paymentOrder.getOrderNumber());//商户订单号
        String amt = paymentTransaction.getPaymentAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        requestMap.put("txn_amt", amt);//交易金额

        return requestMap;
    }

}
