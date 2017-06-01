package com.siebre.payment.paymenthandler.unionpay.pay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymenthandler.unionpay.sdk.UnionPayUtil;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;

/**
 * Created by AdamTang on 2017/4/13.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service("unionPayAcpPaymentHandler")
public class UnionPayAcpPaymentHandler extends AbstractPaymentComponent {
    @Override
    protected PaymentResponse handleInternal(PaymentRequest request, PaymentWay paymentWay, PaymentOrder paymentOrder, PaymentTransaction paymentTransaction) {
        Map<String,String> requestParams = generateParamsMap(request,paymentWay,paymentTransaction);

        String paymentUrl = this.getPaymentUrl(paymentWay,requestParams);

        return PaymentResponse.builder().payUrl(paymentUrl).build();
    }

    private Map<String, String> generateParamsMap(PaymentRequest request, PaymentWay paymentWay, PaymentTransaction paymentTransaction) {
        Map<String, String> requestData = new HashMap<>();

        requestData.put("version", "5.1.0");//版本号，全渠道默认值-------------------
        requestData.put("encoding", "UTF-8");//字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("signMethod", "11");//签名方法 固定11 sha256
        requestData.put("txnType", "01");//交易类型 ，01：消费
        requestData.put("txnSubType", "01");//交易子类型， 01：自助消费
        requestData.put("bizType", "000201");//业务类型，B2C网关支付，手机wap支付
        requestData.put("channelType", "07");//渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机

        requestData.put("merId", paymentWay.getPaymentChannel().getMerchantCode());//商户号码
        requestData.put("accessType", "0");//接入类型，0：直连商户
        requestData.put("orderId", paymentTransaction.getInternalTransactionNumber());//商户订单号，8-40位数字字母，不能含“-”或“_”
        requestData.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(paymentTransaction.getCreateDate()));//订单发送时间，取系统时间，格式为YYYYMMDDhhmmss
        requestData.put("currencyCode", "156");//交易币种（境内商户一般是156 人民币）

        String amt = paymentTransaction.getPaymentAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();

        requestData.put("txnAmt",amt ); //交易金额，单位分，不要带小数点

        requestData.put("frontUrl", paymentWay.getPaymentReturnPageUrl());//前台通知地址
        requestData.put("backUrl", paymentWay.getPaymentCallbackUrl());//后台通知地址

        requestData.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(paymentTransaction.getCreateDate().getTime() + 30 * 60 * 1000));// 订单超时时间 30分钟

        return UnionPayUtil.sign(requestData, paymentWay.getSecretKey());
    }

}
