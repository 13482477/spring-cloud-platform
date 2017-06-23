package com.siebre.payment.paymenthandler.allinpay.query;

import com.aipg.common.AipgReq;
import com.aipg.common.InfoReq;
import com.aipg.transquery.TransQueryReq;
import com.allinpay.XmlTools;
import com.siebre.payment.paymenthandler.allinpay.sdk.AllinPayTranx;
import com.siebre.payment.paymenthandler.basic.paymentquery.AbstractPaymentQueryComponent;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryRequest;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentway.entity.PaymentWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by meilan on 2017/5/17.
 * 通联实时代扣--查询
 */
@Service("allinPayQueryHandler")
public class AllinPayQueryHandler extends AbstractPaymentQueryComponent {

    @Autowired
    private AllinPayTranx allinPayTranx;

    @Override
    protected void handleInternal(PaymentQueryRequest request, PaymentQueryResponse response) {

        String trx_code = "200004";//交易代码
        boolean isTLTFront = false;//是否发送至前置机（由前置机进行签名）如不特别说明，商户技术不要设置为true

        PaymentWay paymentWay = request.getPaymentWay();
        PaymentInterface paymentInterface = request.getPaymentInterface();

        queryTransaction(paymentInterface.getRequestUrl(), trx_code, isTLTFront, paymentWay, request, response);
    }

    private void queryTransaction(String url, String trx_code, boolean isTLTFront, PaymentWay paymentWay, PaymentQueryRequest request, PaymentQueryResponse response) {
        String xml = "";
        AipgReq aipgReq = new AipgReq();
        InfoReq info = allinPayTranx.makeReq(trx_code, paymentWay);
        aipgReq.setINFO(info);

        TransQueryReq transQueryReq = new TransQueryReq();
        aipgReq.addTrx(transQueryReq);
        transQueryReq.setMERCHANT_ID(paymentWay.getPaymentChannel().getMerchantCode());

        String reqsn = request.getExternalNumber();//外部交易流水号


        transQueryReq.setQUERY_SN(reqsn);//原请求交易中的REQ_SN的值
        transQueryReq.setSTATUS(2);//交易状态条件, 0成功,1失败, 2全部,3退票
        transQueryReq.setTYPE(1);//0.按完成日期1.按提交日期，默认为1

        if (reqsn == null || "".equals(reqsn)) {//若不填QUERY_SN则必填开始日/结束日
            transQueryReq.setSTART_DAY(request.getStartDate().toString());
            transQueryReq.setEND_DAY(request.getEndDate().toString());
        }

        xml = XmlTools.buildXml(aipgReq, true);

        allinPayTranx.dealRetForQuery(allinPayTranx.sendToTlt(xml, isTLTFront, url, paymentWay), trx_code, response);

    }

}
