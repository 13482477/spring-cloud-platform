package com.siebre.payment.paymenthandler.unionpay.query;

import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentQueryComponent;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymenthandler.unionpay.sdk.UnionPayUtil;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.utils.http.HttpTookit;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service("unionPayPaymentQueryHandler")
public class UnionPayPaymentQueryHandler extends AbstractPaymentQueryComponent {

    @Override
    protected void handleInternal(PaymentQueryRequest request, PaymentQueryResponse response) {
        logger.info("进入银联支付查询");

        PaymentWay paymentWay = request.getPaymentWay();

        PaymentInterface paymentInterface = request.getPaymentInterface();

        String url = paymentInterface.getRequestUrl();

        Map<String, String> requestParams = generateParamsMap(request, paymentWay, request.getPaymentTransaction());


        PaymentTransactionStatus status = doPost(url, requestParams);
        response.setStatus(status);
    }

    private Map<String, String> generateParamsMap(PaymentQueryRequest request, PaymentWay paymentWay, PaymentTransaction paymentTransaction) {
        Map<String, String> requestData = new HashMap<String, String>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        requestData.put("version", "5.1.0");//版本号，全渠道默认值-------------------
        requestData.put("encoding", "UTF-8");//字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("signMethod", "11");//签名方法 固定11 sha256
        requestData.put("txnType", "00");///交易类型 00-默认
        requestData.put("txnSubType", "00");//交易子类型  默认00
        requestData.put("bizType", "000201");//业务类型，B2C网关支付，手机wap支付


        /***商户接入参数***/
        requestData.put("merId", paymentWay.getPaymentChannel().getMerchantCode());                  //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        requestData.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改

        /***要调通交易以下字段必须修改***/
        requestData.put("orderId", paymentTransaction.getInternalTransactionNumber());                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
        requestData.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(paymentTransaction.getCreateDate()));                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

        return UnionPayUtil.sign(requestData, paymentWay.getSecretKey());
    }


    private PaymentTransactionStatus doPost(String url, Map<String, String> requestParams) {
        String responseContent = HttpTookit.doPost(url, requestParams);
        logger.info("responseContent:{}", responseContent);
        Map<String, String> result = UnionPayUtil.responseToMap(responseContent);

        String respCode = result.get("respCode");
        if ("00".equals(respCode)) {
            //交易成功，更新商户订单状态
            return PaymentTransactionStatus.PAY_SUCCESS;
        } else if ("03".equals(respCode) ||
                "04".equals(respCode) ||
                "05".equals(respCode)) {
            //需再次发起交易状态查询交易
            return PaymentTransactionStatus.PAY_PROCESSING;
        } else {
            //其他应答码为失败请排查原因
            return PaymentTransactionStatus.PAY_FAILED;
        }
    }
}
