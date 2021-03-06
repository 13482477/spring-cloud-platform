package com.siebre.payment.paymenthandler.alipay.paycallback;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.siebre.basic.service.ServiceResult;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenthandler.basic.paymentcallback.AbstractPaymentCallBackHandler;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Huang Tianci
 *         支付宝--手机网站支付--回调处理
 */
@Component("alipayTradeWapCallBackHandler")
public class AlipayTradeWapCallBackHandler extends AbstractPaymentCallBackHandler {

    @Autowired
    private PaymentWayMapper paymentWayMapper;

    @Override
    protected Object callBackHandleInternal(HttpServletRequest request, HttpServletResponse response, PaymentInterface paymentInterface) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = getAliCallBackParams(request);
        String responseStr = JsonUtil.mapToJson(params);
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表
        try {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            //计算得出通知验证结果
            PaymentWay paymentWay = paymentWayMapper.selectByPrimaryKey(paymentInterface.getPaymentWayId());//paymentInterface.getPaymentWay();
            boolean verify_result = AlipaySignature.rsaCheckV1(params, paymentWay.getPublicKey(), AlipayConfig.INPUT_CHARSET_UTF, paymentWay.getEncryptionMode().getDescription());
            //验证成功
            if (verify_result) {
                String seller_id = new String(request.getParameter("seller_id").getBytes("ISO-8859-1"), "UTF-8");
                BigDecimal total_amount = new BigDecimal(new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8"));

                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    try {
                        Date successDate = f.parse(request.getParameter("gmt_payment"));
                        ServiceResult<PaymentTransaction> result = this.paymentTransactionService.paymentConfirm(out_trade_no, trade_no, seller_id, total_amount, successDate, responseStr);
                        if(result.getSuccess()) {
                            realTimeReconcileProduct.sendToRealTimeExchange(out_trade_no);
                        }
                    } catch (ParseException e) {
                        logger.error("日期转换失败");
                        e.printStackTrace();
                    }

                }
                //response.getWriter().println("success");	//请不要修改或删除
                return "success";
            } else {//验证失败
                //response.getWriter().println("fail");
                return "fail";
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持的字符编码", e);
        } catch (AlipayApiException e) {
            logger.error("回调验签异常", e);
        } catch (IOException e) {
            logger.error("回调验签异常", e);
        }
        return "fail";
    }

    private Map<String, String> getAliCallBackParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }
}
