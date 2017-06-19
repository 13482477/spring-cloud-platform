package com.siebre.payment.paymenthandler.allinpay.refund;

import com.aipg.common.AipgReq;
import com.aipg.common.InfoReq;
import com.aipg.refund.Refund;
import com.allinpay.XmlTools;
import com.siebre.payment.paymenthandler.allinpay.sdk.AllinPayTranx;
import com.siebre.payment.paymenthandler.basic.paymentrefund.AbstractPaymentRefundComponent;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by meilan on 2017/5/7.
 * 通联实时代扣--退款
 */
@Component("allinPayRealTimeRefundHandler")
public class AllinPayRealTimeRefundHandler extends AbstractPaymentRefundComponent {

    @Autowired
    private AllinPayTranx allinPayTranx;

    @Override
    protected void handleInternal(PaymentRefundRequest paymentRefundRequest, PaymentRefundResponse refundResponse) {



        String trx_code = "REFUND";//交易代码
        boolean isTLTFront = false;//是否发送至前置机（由前置机进行签名）如不特别说明，商户技术不要设置为true

        refundTranx(trx_code, isTLTFront, paymentRefundRequest, refundResponse);
    }

    private void refundTranx(String trx_code, boolean isTLTFront, PaymentRefundRequest paymentRefundRequest, PaymentRefundResponse refundResponse) {
        PaymentOrder paymentOrder = paymentRefundRequest.getPaymentOrder();
        PaymentWay paymentWay = paymentRefundRequest.getPaymentWay();
        PaymentInterface paymentInterface = paymentRefundRequest.getPaymentInterface();

        String xml = "";
        AipgReq aipg = new AipgReq();
        InfoReq info = allinPayTranx.makeReq(trx_code, paymentWay);
        aipg.setINFO(info);

        Refund refund = new Refund();
        refund.setBUSINESS_CODE("09200");
        //refund.setBUSINESS_CODE("00600");
        refund.setMERCHANT_ID(paymentWay.getPaymentChannel().getMerchantCode());//商户号
        String oldNumber = paymentRefundRequest.getOriginExternalNumber();
        refund.setORGBATCHID(oldNumber); //原交易的REQ_SN 交易的文件名
        refund.setORGBATCHSN("0");//原交易的记录序号，原交易为单笔实时交易时填0 实时收款设置为0
        refund.setACCOUNT_NO("6214850218622493");
        refund.setACCOUNT_NAME("张三");
        String amt = paymentOrder.getAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        refund.setAMOUNT(amt);
        //refund.setREMARK("全部退还");
        aipg.addTrx(refund);

        xml = XmlTools.buildXml(aipg, true);

        allinPayTranx.dealRetForRefund(allinPayTranx.sendToTlt(xml, isTLTFront, paymentInterface.getRequestUrl(), paymentWay), trx_code, paymentRefundRequest, refundResponse);
    }

}
