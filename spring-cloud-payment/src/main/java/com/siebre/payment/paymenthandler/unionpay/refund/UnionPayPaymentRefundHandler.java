package com.siebre.payment.paymenthandler.unionpay.refund;

import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.paymenthandler.basic.paymentrefund.AbstractPaymentRefundComponent;
import com.siebre.payment.paymenthandler.unionpay.sdk.UnionPayUtil;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.utils.http.HttpTookit;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/24.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service("unionPayPaymentRefundHandler")
public class UnionPayPaymentRefundHandler extends AbstractPaymentRefundComponent {

    @Override
    protected void handleInternal(PaymentRefundRequest paymentRefundRequest,PaymentRefundResponse refundResponse, PaymentTransaction paymentTransaction, PaymentOrder paymentOrder, PaymentWay paymentWay, PaymentInterface paymentInterface) {
        Map<String, String> requestData = generateParamsMap(paymentRefundRequest, paymentWay, paymentInterface, paymentTransaction);

        String url = paymentInterface.getRequestUrl();
        logger.info("请求地址{}", url);

        doPost(paymentRefundRequest, refundResponse, url, requestData);
    }


    private Map<String, String> generateParamsMap(PaymentRefundRequest paymentRefundRequest, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction) {
        Map<String, String> requestData = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        requestData.put("version", "5.1.0");//版本号，全渠道默认值-------------------
        requestData.put("encoding", "UTF-8");//字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("signMethod", "11");//签名方法 固定11 sha256
        requestData.put("txnType", "04");//交易类型 ，01：消费 04-退货
        requestData.put("txnSubType", "00");//交易子类型，默认00		 01：自助消费
        requestData.put("bizType", "000201");//业务类型，B2C网关支付，手机wap支付
        requestData.put("channelType", "07");//渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机

        /***商户接入参数***/
        requestData.put("merId", paymentWay.getPaymentChannel().getMerchantCode());                //商户号码
        requestData.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改
        requestData.put("orderId", paymentTransaction.getInternalTransactionNumber());          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
        requestData.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）

        String amt = paymentRefundRequest.getRefundApplication().getRefundAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();

        requestData.put("txnAmt", amt);                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额

        requestData.put("backUrl", paymentInterface.getCallbackUrl());      //银联通用后台通知地址

        requestData.put("origQryId", paymentRefundRequest.getOriginExternalNumber());//****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

        return UnionPayUtil.sign(requestData, paymentWay.getSecretKey());
    }

    private void doPost(PaymentRefundRequest paymentRefundRequest, PaymentRefundResponse refundResponse, String url, Map<String, String> requestParams) {
        String responseContent = HttpTookit.doPost(url, requestParams);
        logger.info("responseContent:{}", responseContent);
        Map<String, String> result = UnionPayUtil.responseToMap(responseContent);

        refundResponse.setSynchronize(false);//银联为异步返回结果

        PaymentTransaction refundTransaction = paymentRefundRequest.getRefundTransaction();
        RefundApplication refundApplication = paymentRefundRequest.getRefundApplication();

        refundResponse.setExternalTransactionNumber(result.get("queryId"));
        refundTransaction.setExternalTransactionNumber(result.get("queryId"));
        refundResponse.setReturnMessage(result.get("respMsg"));
        String respCode = result.get("respCode");
        if ("00".equals(respCode)) {
            refundTransaction.setPaymentStatus(PaymentTransactionStatus.SUCCESS);//退款交易调用成功
            refundApplication.setStatus(RefundApplicationStatus.SUCCESS);
            refundResponse.setRefundApplicationStatus(RefundApplicationStatus.SUBMITTED);
        } else if ("03".equals(respCode) ||
                "04".equals(respCode) ||
                "05".equals(respCode)) {
            //TODO 未成功。后续需发起交易状态查询交易确定交易状态
            refundTransaction.setPaymentStatus(PaymentTransactionStatus.PROCESSING);
            refundApplication.setStatus(RefundApplicationStatus.PROCESSING);
            refundResponse.setRefundApplicationStatus(RefundApplicationStatus.PROCESSING);
        } else {
            refundTransaction.setPaymentStatus(PaymentTransactionStatus.FAILED);
            refundApplication.setStatus(RefundApplicationStatus.FAILED);
            refundResponse.setRefundApplicationStatus(RefundApplicationStatus.FAILED);
        }

        refundResponse.setRefundApplication(refundApplication);
        refundResponse.setPaymentTransaction(refundTransaction);
    }
}
