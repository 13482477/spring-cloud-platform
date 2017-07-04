package com.siebre.payment.paymenthandler.allinpay.sdk;

import com.aipg.common.AipgRsp;
import com.aipg.common.InfoReq;
import com.aipg.common.XSUtil;
import com.aipg.rtrsp.TransRet;
import com.aipg.transquery.QTDetail;
import com.aipg.transquery.QTransRsp;
import com.allinpay.XmlTools;
import com.siebre.basic.exception.SiebreRuntimeException;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meilan on 2017/5/16.
 *通联实时代扣（支付，退款，查询）
 */
@Component("allinPayTranx")
public class AllinPayTranx {

    private Logger logger = LoggerFactory.getLogger(AllinPayTranx.class);

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    /**
     * 支付返回报文处理逻辑
     */
    public Map<String, String> dealRetForPay(String retXml, String trxcode, PaymentTransaction paymentTransaction, PaymentWay paymentWay) {

        AipgRsp aipgrsp = null;
        aipgrsp = XSUtil.parseRsp(retXml);

        String responseStr = JsonUtil.toJson(aipgrsp, true);

        paymentTransaction.setResponseStr(JsonUtil.toJson(aipgrsp, true));

        Map<String, String> result = new HashMap<>();
        String internalTransactionNumber = paymentTransaction.getInternalTransactionNumber();
        String externalTransactionNumber = aipgrsp.getINFO().getREQ_SN();
        //实时交易结果返回处理逻辑(包括单笔实时代收，单笔实时代付，单笔实时身份验证)
        if ("100011".equals(trxcode) || "100014".equals(trxcode) || "100400".equals(trxcode)) {
            if ("0000".equals(aipgrsp.getINFO().getRET_CODE())) {
                TransRet ret = (TransRet) aipgrsp.getTrxData().get(0);
                logger.info("transaction result =" + ret.getRET_CODE() + ":" + ret.getERR_MSG());
                if ("0000".equals(ret.getRET_CODE())) {
                    logger.info("transaction success(last result)");
                    //修改订单交易状态
                    paymentTransaction.setExternalTransactionNumber(externalTransactionNumber);
                    paymentTransaction.setPaymentStatus(PaymentTransactionStatus.PAY_SUCCESS);
                    paymentTransaction.setResponseStr(responseStr);

                    //修改订单交易状态
                    String seller_id = paymentWay.getPaymentChannel().getMerchantCode();
                    BigDecimal total_fee = paymentTransaction.getPaymentAmount();
                    paymentTransactionService.paymentConfirm(internalTransactionNumber, externalTransactionNumber, seller_id, total_fee, new Date(), responseStr);

                    result.put("transaction_result", ReturnCode.SUCCESS.getDescription());
                    result.put("orderNumber", internalTransactionNumber);
                    result.put("msg", ret.getERR_MSG());
                    return result;
                } else {
                    logger.info("transaction fail(last result)");
                    logger.info("transaction fail reason：" + ret.getERR_MSG());
                }
            } else if ("2000".equals(aipgrsp.getINFO().getRET_CODE())
                    || "2001".equals(aipgrsp.getINFO().getRET_CODE())
                    || "2003".equals(aipgrsp.getINFO().getRET_CODE())
                    || "2005".equals(aipgrsp.getINFO().getRET_CODE())
                    || "2007".equals(aipgrsp.getINFO().getRET_CODE())
                    || "2008".equals(aipgrsp.getINFO().getRET_CODE())) {
                logger.info("Transaction processing or uncertain state, need to be in the next 5 minutes after the transaction results query (polling)");
            } else if (aipgrsp.getINFO().getRET_CODE().startsWith("1")) {
                String errormsg = aipgrsp.getINFO().getERR_MSG() == null ? "connection has problem,please try again later" : aipgrsp.getINFO().getERR_MSG();
                logger.info("transaction request fail, reason：" + errormsg);
            } else {
                TransRet ret = (TransRet) aipgrsp.getTrxData().get(0);
                logger.info("transaction fail(last result), reason：" + ret.getERR_MSG());
            }
        }

        paymentTransactionService.setFailStatus(internalTransactionNumber,externalTransactionNumber);
        result.put("transaction_result", ReturnCode.FAIL.getDescription());
        result.put("orderNumber", internalTransactionNumber);
        result.put("msg", aipgrsp.getINFO().getERR_MSG());
        return result;
    }

    /**
     * 退款返回报文处理逻辑
     */
    public void dealRetForRefund(String retXml, String trxcode, PaymentRefundRequest paymentRefundRequest, PaymentRefundResponse  refundResponse){

        AipgRsp aipgrsp = null;
        aipgrsp = XSUtil.parseRsp(retXml);

        String externalTransactionNumber = aipgrsp.getINFO().getREQ_SN();
        refundResponse.setExternalTransactionNumber(externalTransactionNumber);
        //TransRet ret = (TransRet) aipgrsp.getTrxData().get(0);
       // refundResponse.setReturnMessage(ret.getERR_MSG());

        PaymentTransaction refundTransaction = paymentRefundRequest.getRefundTransaction();
        RefundApplication refundApplication = paymentRefundRequest.getRefundApplication();

        //交易退款返回处理逻辑
        if("REFUND".equals(trxcode)){
            if("0000".equals(aipgrsp.getINFO().getRET_CODE())){//退款交易调用成功
                refundTransaction.setExternalTransactionNumber(externalTransactionNumber);
                refundTransaction.setPaymentStatus(PaymentTransactionStatus.REFUND_SUCCESS);

                refundApplication.setStatus(RefundApplicationStatus.SUCCESS);
                refundApplication.setResponse(RefundApplicationStatus.SUCCESS.getDescription());

                refundResponse.setReturnCode(ReturnCode.SUCCESS.getDescription());
                refundResponse.setReturnMessage("退款成功");
                refundResponse.setRefundApplicationStatus(RefundApplicationStatus.SUCCESS);
                logger.info("refund success");

            }
            else{
                String faileReason = "退款失败,失败原因:" + aipgrsp.getINFO().getERR_MSG();
                refundTransaction.setExternalTransactionNumber(externalTransactionNumber);
                refundTransaction.setPaymentStatus(PaymentTransactionStatus.REFUND_FAILED);

                refundApplication.setStatus(RefundApplicationStatus.FAILED);
                refundApplication.setResponse(faileReason);

                refundResponse.setReturnCode(ReturnCode.FAIL.getDescription());
                refundResponse.setReturnMessage(faileReason);
                refundResponse.setRefundApplicationStatus(RefundApplicationStatus.FAILED);
                logger.info(faileReason);
            }
        }

        refundResponse.setRefundApplication(refundApplication);
        refundResponse.setRefundTransaction(refundTransaction);
    }

    /**
     * 查询返回报文处理逻辑
     */
    public void dealRetForQuery(String retXml, String trxcode, PaymentQueryResponse response){

        AipgRsp aipgrsp = null;
        aipgrsp = XSUtil.parseRsp(retXml);

        //交易查询处理逻辑(交易结果查询--200004,交易明细查询--200005)
        if("200004".equals(trxcode)||"200005".equals(trxcode)){
            if("0000".equals(aipgrsp.getINFO().getRET_CODE())){
                QTransRsp qrsq=(QTransRsp) aipgrsp.getTrxData().get(0);
                logger.info("query success !");
                List<QTDetail> details=qrsq.getDetails();
                for(QTDetail lobj:details){
                    logger.info("原支付交易批次号:"+lobj.getBATCHID()+"  ");
                    logger.info("记录序号:"+lobj.getSN()+"  ");
                    logger.info("账号:"+lobj.getACCOUNT_NO()+"  ");
                    logger.info("户名:"+lobj.getACCOUNT_NAME()+"  ");
                    logger.info("金额:"+lobj.getAMOUNT()+"  ");
                    logger.info("返回结果:"+lobj.getRET_CODE()+"  ");

                    if("0000".equals(lobj.getRET_CODE())){
                        logger.info("返回说明:交易成功 ");
                        logger.info("更新交易库状态（原交易的状态）");
                    }else{
                        logger.info("返回说明:"+lobj.getERR_MSG()+"  ");
                        logger.info("更新交易库状态（原交易的状态）");
                    }
                }

                response.setStatus(PaymentTransactionStatus.REFUND_SUCCESS);//设置状态--交易成功

            }else if("2000".equals(aipgrsp.getINFO().getRET_CODE())
                    ||"2001".equals(aipgrsp.getINFO().getRET_CODE())
                    ||"2003".equals(aipgrsp.getINFO().getRET_CODE())
                    ||"2005".equals(aipgrsp.getINFO().getRET_CODE())
                    ||"2007".equals(aipgrsp.getINFO().getRET_CODE())
                    ||"2008".equals(aipgrsp.getINFO().getRET_CODE())){
                logger.info("返回说明:"+aipgrsp.getINFO().getRET_CODE()+"  ");
                logger.info("返回说明："+aipgrsp.getINFO().getERR_MSG());
                logger.info("该状态时，说明整个批次的交易都在处理中");
            }else if("2004".equals(aipgrsp.getINFO().getRET_CODE())){
                logger.info("整批交易未受理通过（最终失败）");
            }else if("1002".equals(aipgrsp.getINFO().getRET_CODE())){
                logger.info("查询无结果集（表示通联端根据商户请求上送的条件查不到对应的结果集）");
            }else{
                logger.info("查询请求失败，请重新发起查询");
            }

            response.setStatus(PaymentTransactionStatus.REFUND_FAILED);//设置状态--交易失败

        }
    }

    /**
     * 组装报文头部
     * @param trxcod
     * @return
     */
    public InfoReq makeReq(String trxcod, PaymentWay paymentWay) {
        InfoReq info = new InfoReq();
        info.setTRX_CODE(trxcod);//交易代码 100011
        info.setREQ_SN(paymentWay.getPaymentChannel().getMerchantCode() + "-" + String.valueOf(System.currentTimeMillis()));
        //info.setUSER_NAME(paymentWay.getPaymentChannel().getMerchantName());
        //info.setUSER_PASS(paymentWay.getPaymentChannel().getMerchantPwd());
        info.setUSER_NAME("20036800000096104");
        info.setUSER_PASS("111111");
        //info.setUSER_NAME("20060400000044502");//demo测试
        //info.setUSER_PASS("`12qwe");//demo测试
        info.setLEVEL("5");//处理级别 0-9 0优先级最低
        info.setDATA_TYPE("2");//2：xml格式
        info.setVERSION("03");
        //info.setMERCHANT_ID(paymentWay.getPaymentChannel().getMerchantCode());

        return info;
    }

    public String sendToTlt(String xml, boolean flag, String url, PaymentWay paymentWay) {
        try {
            if (!flag) {
                xml = this.signMsg(xml,paymentWay);
            } else {
                xml = xml.replaceAll("<SIGNED_MSG></SIGNED_MSG>", "");
            }
            logger.info("signMsg request xml success! ");
            return sendXml(xml, url, flag);
        } catch (Exception e) {
            logger.error("Payment request network signMsg error!", e);
            throw new SiebreRuntimeException("Payment request network signMsg error!");
        }
    }

    /**
     * 报文签名
     */
    public String signMsg(String xml,PaymentWay paymentWay) throws Exception {
        xml = XmlTools.signMsg(xml, this.getClass().getResource("/").getPath() + "20036800000096104.p12", paymentWay.getSecretKey(), false);
        //xml = XmlTools.signMsg(xml, this.getClass().getResource("/").getPath() + "20060400000044502.p12", "111111", false);
        return xml;
    }

    public String sendXml(String xml, String url, boolean isFront) {
        logger.info("======================send request xml======================：\n" + xml);
        String resp = null;
        try {
            resp = XmlTools.send(url, xml);
        } catch (Exception e) {
            logger.error("Payment request network send request xml error!", e);
            throw new SiebreRuntimeException("Payment request network send request xml error!");
        }
        logger.info("======================response======================");
        try {
            boolean flag = this.verifyMsg(resp, this.getClass().getResource("/").getPath() + "allinpay-pds.cer", isFront);
            if (flag) {
                logger.info("Response content validation passed");
            } else {
                logger.info("Response content validation failed");
            }
        } catch (Exception e) {
            logger.error("Payment response network verifyMsg error!", e);
            throw new SiebreRuntimeException("Payment response network verifyMsg error!");
        }

        return resp;
    }

    /**
     * 验证签名
     */
    public boolean verifyMsg(String msg, String cer, boolean isFront) throws Exception {
        boolean flag = XmlTools.verifySign(msg, cer, false, isFront);
        logger.info("sign result[" + flag + "]");
        return flag;
    }


    


}
