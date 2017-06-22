package com.siebre.payment.paymenthandler.unionpay.paycallback;

import com.siebre.basic.utils.HttpServletRequestUtil;
import com.siebre.payment.paymenthandler.basic.paymentcallback.AbstractPaymentCallBackHandler;
import com.siebre.payment.paymenthandler.unionpay.sdk.UnionPayUtil;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/17.
 * Project:siebre-cloud-platform
 * Version:1.0
 */

@Component("unionPayCallBackHandler")
public class UnionPayCallBackHandler extends AbstractPaymentCallBackHandler {

    @Autowired
    private PaymentWayMapper paymentWayMapper;

    @Override
    protected Object callBackHandleInternal(HttpServletRequest request, HttpServletResponse response,PaymentInterface paymentInterface) {

        Map<String,String > paramsMap = HttpServletRequestUtil.getParameterMap(request);

        PaymentWay paymentWay  = paymentWayMapper.selectByPrimaryKey(paymentInterface.getPaymentWayId()); //paymentInterface.getPaymentWay();

        if(UnionPayUtil.validateSign(paramsMap,paymentWay.getSecretKey())){
            logger.info("银联签名验证成功!");
            processUnionPayReturnType(paramsMap,response);
        }else{
            logger.info("银联签名验证失败");
        }
        return null;
    }

    private void processUnionPayReturnType(Map<String,String> paramsMap,HttpServletResponse response){
        String returnType =  paramsMap.get("txnType");

        if("01".equals(returnType)){
            //消费类型
            processTransactionAndOrder(paramsMap,response);

        }else if ("04".equals(returnType)){
            //退款类型
            processRefundApplication(paramsMap,response);
        }
    }

    private void processTransactionAndOrder(Map<String,String> paramsMap,HttpServletResponse response){
        logger.info("银联支付回调成功");

        String respCode =  paramsMap.get("respCode"); //判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。

        String orderId = paramsMap.get("orderId"); //获取后台通知的数据

        String queryId = paramsMap.get("queryId");

        if("00".equals(respCode)){//支付成功
            String merId = paramsMap.get("merId");
            BigDecimal txnAmt = new BigDecimal(paramsMap.get("txnAmt")).divide(new BigDecimal("100"));
            //TODO  支付成功时间从返回报文中取
            this.paymentTransactionService.paymentConfirm(orderId,queryId , merId, txnAmt, new Date());
        }
    }

    private void processRefundApplication(Map<String,String> paramsMap,HttpServletResponse response){
        logger.info("银联退款回调成功");
        String respCode =  paramsMap.get("respCode"); //判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
        String orderId = paramsMap.get("orderId"); //获取后台通知的数据
        String queryId = paramsMap.get("queryId");

        if("00".equals(respCode)){//退款成功
            this.paymentTransactionService.refundConfirm(orderId,queryId);
        }
    }

}
