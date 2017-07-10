package com.siebre.payment.paymenthandler.allinpay.pay;

import com.aipg.common.AipgReq;
import com.aipg.common.InfoReq;
import com.aipg.rtreq.Trans;
import com.allinpay.XmlTools;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.entity.enums.SubsequentAction;
import com.siebre.payment.paymentaccount.entity.PaymentAccount;
import com.siebre.payment.paymenthandler.allinpay.sdk.AllinPayTranx;
import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by meilan on 2017/5/8.
 * 通联实时代扣--支付
 */
@Component("allinPayRealTimeHandler")
public class AllinPayRealTimeHandler extends AbstractPaymentComponent {

    //private static String p12Url = "D:/20036800000096104.p12";
    //private static String cerUrl = "D:/allinpay-pds.cer";
    //private static String p12Url = "D:/20060400000044502.p12";//demo里的测试证书
    private Logger logger = LoggerFactory.getLogger(AllinPayRealTimeHandler.class);

    @Autowired
    private AllinPayTranx allinPayTranx ;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Override
    protected void handleInternal(PaymentRequest request, PaymentResponse response, PaymentWay paymentWay, PaymentInterface paymentInterface, PaymentTransaction paymentTransaction) {

        String trx_code = "100011";//交易代码
        boolean isTLTFront = false;//是否发送至前置机（由前置机进行签名）如不特别说明，商户技术不要设置为true

        Map<String, String> result = singleTranx(request, paymentInterface.getRequestUrl(), trx_code, isTLTFront, paymentWay, paymentTransaction);

        response.setReturnCode(result.get("transaction_result"));
        response.setReturnMessage(result.get("msg"));
        if(ReturnCode.SUCCESS.getDescription().equals(result.get("transaction_result"))) {
            paymentOrderService.updateOrderStatus(request.getPaymentOrder(), PaymentOrderPayStatus.PAID, new Date());
            paymentTransactionService.updateBySelective(paymentTransaction);
            response.setSubsequentAction(SubsequentAction.READ_PAY_RESULT.getValue());
        }

    }

    private Map<String, String> singleTranx(PaymentRequest request, String url, String trx_code, boolean isTLTFront, PaymentWay paymentWay, PaymentTransaction paymentTransaction) {
        PaymentOrder paymentOrder = request.getPaymentOrder();
        PaymentAccount account = paymentOrder.getPaymentAccount();
        String xml = "";
        AipgReq aipg = new AipgReq();
        InfoReq info = allinPayTranx.makeReq(trx_code, paymentWay);
        aipg.setINFO(info);

        Trans trans = new Trans();
        trans.setBUSINESS_CODE("19900");//业务代码
        trans.setMERCHANT_ID(paymentWay.getPaymentChannel().getMerchantCode());
        trans.setSUBMIT_TIME(new SimpleDateFormat("yyyyMMddHHmmss").format(paymentTransaction.getCreateDate()));
        trans.setACCOUNT_NAME(account.getHolderName());//账号名 银行卡或存折上的所有人姓名。
        trans.setACCOUNT_NO(account.getAcountNumber());//账号 银行卡或存折号码
        trans.setACCOUNT_PROP("0");//账号属性 0私人，1公司。不填时，默认为私人0。
//		trans.setACCOUNT_TYPE("01");//账号类型 00银行卡，01存折，02信用卡。不填默认为银行卡00
        String amt = paymentTransaction.getPaymentAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        trans.setAMOUNT(amt);
//      trans.setBANK_CODE("0308");//银行代码 银行代码，存折必须填写。
//      trans.setCURRENCY("CNY");//货币类型 人民币：CNY, 港元：HKD，美元：USD。不填时，默认为人民币。
//      trans.setCUST_USERID("252523524253xx");//自定义用户号 商户自定义的用户号，开发人员可当作备注字段使用
//      trans.setTEL("");//手机号/小灵通 小灵通带区号，不带括号，减号
        aipg.addTrx(trans);

        xml = XmlTools.buildXml(aipg, true);
        logger.info("request data: {}", xml);
        paymentTransaction.setRequestStr(JsonUtil.mapToJson(ConvertToXML.toMap(xml)));

        Map<String, String> result = allinPayTranx.dealRetForPay(allinPayTranx.sendToTlt(xml, isTLTFront, url, paymentWay), trx_code, paymentTransaction, paymentWay);

        return result;

    }



}
