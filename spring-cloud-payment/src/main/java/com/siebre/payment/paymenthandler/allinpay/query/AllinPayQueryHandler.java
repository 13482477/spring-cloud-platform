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
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentway.entity.PaymentWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by meilan on 2017/5/17.
 * 通联实时代扣--交易结果查询
 * 文档地址：http://113.108.182.3:8282/techsp/helper/filedetail/tlt/filedetail134.html
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

        queryTransaction(paymentInterface.getRequestUrl(), trx_code, isTLTFront, paymentWay, response);
    }

    private void queryTransaction(String url, String trx_code, boolean isTLTFront, PaymentWay paymentWay, PaymentQueryResponse response) {
        PaymentOrder order = response.getLocalOrder();
        String xml = "";
        AipgReq aipgReq = new AipgReq();
        InfoReq info = allinPayTranx.makeReq(trx_code, paymentWay);
        aipgReq.setINFO(info);

        TransQueryReq transQueryReq = new TransQueryReq();
        aipgReq.addTrx(transQueryReq);
        transQueryReq.setMERCHANT_ID(paymentWay.getPaymentChannel().getMerchantCode());

        String reqsn = order.getExternalOrderNumber();//外部交易流水号


        transQueryReq.setQUERY_SN(reqsn);//原请求交易中的REQ_SN的值
        transQueryReq.setSTATUS(2);//交易状态条件, 0成功,1失败, 2全部,3退票
        transQueryReq.setTYPE(1);//0.按完成日期1.按提交日期，默认为1

        xml = XmlTools.buildXml(aipgReq, true);

        allinPayTranx.dealRetForQuery(allinPayTranx.sendToTlt(xml, isTLTFront, url, paymentWay), trx_code, response);

    }

}
