package com.siebre.payment.paymenthandler.alipay.paycallback;

import com.siebre.basic.utils.HttpServletRequestUtil;
import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipaySign;
import com.siebre.payment.paymenthandler.basic.paymentcallback.AbstractPaymentCallBackHandler;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;
import com.siebre.payment.utils.messageconvert.Converts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AdamTang on 2017/4/17.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Component("alipayCallBackHandler")
public class AlipayCallBackHandler extends AbstractPaymentCallBackHandler {

    @Autowired
    private PaymentWayMapper paymentWayMapper;

    private static final String SIGN_PARAM="sign";

    private static final String SIGN_TYPE_PARAM="sign_type";

    @Override
    protected Object callBackHandleInternal(HttpServletRequest request, HttpServletResponse response,PaymentInterface paymentInterface) {

        AlipayWebPaymentCallbackDto payNotifyParamDTO = new AlipayWebPaymentCallbackDto();

        Map<String, String> paramMap = HttpServletRequestUtil.getParameterMap(request);

        PaymentWay paymentWay = paymentWayMapper.selectByPrimaryKey(paymentInterface.getPaymentWayId());

        if (validateSign(paramMap, paymentWay)) {

            logger.info("支付宝验签成功");
            String status = paramMap.get("trade_status");

            if ("TRADE_FINISHED".equals(status) || "TRADE_SUCCESS".equals(status)) {
                payNotifyParamDTO = (AlipayWebPaymentCallbackDto) Converts.convertMap(AlipayWebPaymentCallbackDto.class, paramMap);
                String internalTransactionNumber = payNotifyParamDTO.getOutTradeNo();
                String externalTransactionNumber = payNotifyParamDTO.getTrade_no();
                String seller_id = payNotifyParamDTO.getSeller_id();
                BigDecimal total_fee = new BigDecimal(payNotifyParamDTO.getTotal_fee());

                DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date successDate = f.parse(request.getParameter("gmt_payment"));
                    this.paymentTransactionService.paymentConfirm(internalTransactionNumber, externalTransactionNumber, seller_id, total_fee, successDate);
                } catch (ParseException e) {
                    logger.error("日期转换失败");
                    e.printStackTrace();
                }
                return "success";
            }

        }else{
            logger.info("支付宝验签失败");
        }
        return "fail";
    }


    private boolean validateSign(Map<String, String> paramMap, PaymentWay paymentWay) {
        if(StringUtils.isBlank(paramMap.get(SIGN_PARAM))) {
            return false;
        }

        HashMap<String, String> filterMap = new HashMap<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (entry.getKey().equals(SIGN_PARAM) || entry.getKey().equals(SIGN_TYPE_PARAM)) {
                continue;
            }
            if (StringUtils.isNotBlank(entry.getValue())) {
                filterMap.put(entry.getKey(), entry.getValue());
            }
        }
        String sign = null;
        if (EncryptionMode.MD5.equals(paymentWay.getEncryptionMode())) {
            sign = AlipaySign.createMd5SignOrder(filterMap, paymentWay.getSecretKey());

        } else if (EncryptionMode.RSA.equals(paymentWay.getEncryptionMode())) {

            sign = AlipaySign.createRsaSignOrder(filterMap, paymentWay.getSecretKey());
        }

        return StringUtils.equals(sign,paramMap.get(SIGN_PARAM));

    }
}
