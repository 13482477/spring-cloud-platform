package com.siebre.payment.paymentchannel.transfer;

import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentChannelStatus;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymentchannel.vo.AliPayConfigVo;
import com.siebre.payment.paymentchannel.vo.AliPayWayConfigVo;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentinterface.service.PaymentInterfaceService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Huang Tianci
 * 支付宝配置信息转换成渠道模型的转换类
 */
@Service("alipayConfigTransfer")
public class AlipayConfigTransfer {

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    private PaymentWayService paymentWayService;

    @Autowired
    private PaymentInterfaceService paymentInterfaceService;

    @Transactional("db")
    public ServiceResult<AliPayConfigVo> transfer(AliPayConfigVo aliPayConfigVo) {
        Date current = new Date();
        PaymentChannel paymentChannel = paymentChannelService.queryByChannelCode(AlipayConfig.CHANNEL_CODE).getData();
        if (paymentChannel == null) {
            paymentChannel = initPaymentChannel();
        }
        paymentChannel.setMerchantCode(aliPayConfigVo.getpId());
        paymentChannel.setPayeeAccount(aliPayConfigVo.getSellerAccount());
        paymentChannel.setUpdateDate(current);
        paymentChannelService.updateById(paymentChannel);

        PaymentWay fastPay = paymentWayService.getPaymentWay(AlipayConfig.WAP_WEB_PAY);
        fastPay.setEncryptionMode(aliPayConfigVo.getFastPayWay().getEncryptionMode());
        fastPay.setSecretKey(aliPayConfigVo.getFastPayWay().getSecretKey());
        fastPay.setPublicKey(aliPayConfigVo.getFastPayWay().getPublicKey());
        fastPay.setUpdateDate(current);
        paymentWayService.updatePaymentWay(fastPay);

        PaymentWay tradeWapPayWay = paymentWayService.getPaymentWay(AlipayConfig.WAP_TRADE_PAY);
        tradeWapPayWay.setAppId(aliPayConfigVo.getTradeWapPayWay().getAppId());
        tradeWapPayWay.setEncryptionMode(aliPayConfigVo.getTradeWapPayWay().getEncryptionMode());
        tradeWapPayWay.setSecretKey(aliPayConfigVo.getTradeWapPayWay().getSecretKey());
        tradeWapPayWay.setPublicKey(aliPayConfigVo.getTradeWapPayWay().getPublicKey());
        tradeWapPayWay.setUpdateDate(current);
        paymentWayService.updatePaymentWay(tradeWapPayWay);

        return ServiceResult.<AliPayConfigVo>builder().success(Boolean.TRUE).data(aliPayConfigVo).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    public ServiceResult<AliPayConfigVo> transferToVo(){
        AliPayConfigVo aliPayConfigVo = new AliPayConfigVo();
        PaymentChannel paymentChannel = paymentChannelService.queryByChannelCode(AlipayConfig.CHANNEL_CODE).getData();
        aliPayConfigVo.setpId(paymentChannel.getMerchantCode());
        aliPayConfigVo.setSellerAccount(paymentChannel.getPayeeAccount());

        PaymentWay fastPay = paymentWayService.getPaymentWay(AlipayConfig.WAP_WEB_PAY);
        AliPayWayConfigVo fastPayVo = new AliPayWayConfigVo();
        fastPayVo.setEncryptionMode(fastPay.getEncryptionMode());
        fastPayVo.setSecretKey(fastPay.getSecretKey());
        fastPayVo.setPublicKey(fastPay.getPublicKey());
        aliPayConfigVo.setFastPayWay(fastPayVo);

        PaymentWay tradeWapPayWay = paymentWayService.getPaymentWay(AlipayConfig.WAP_TRADE_PAY);
        AliPayWayConfigVo tradeVo = new AliPayWayConfigVo();
        tradeVo.setAppId(tradeWapPayWay.getAppId());
        tradeVo.setEncryptionMode(tradeWapPayWay.getEncryptionMode());
        tradeVo.setSecretKey(tradeWapPayWay.getSecretKey());
        tradeVo.setPublicKey(tradeWapPayWay.getPublicKey());
        aliPayConfigVo.setTradeWapPayWay(tradeVo);

        return ServiceResult.<AliPayConfigVo>builder().success(Boolean.TRUE).data(aliPayConfigVo).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    /**
     * 初始化支付渠道
     * @return
     */
    @Transactional("db")
    private PaymentChannel initPaymentChannel() {
        Date current = new Date();
        //支付渠道
        PaymentChannel paymentChannel = new PaymentChannel();
        paymentChannel.setChannelName("支付宝");
        paymentChannel.setChannelCode(AlipayConfig.CHANNEL_CODE);
        paymentChannel.setStatus(PaymentChannelStatus.ENABLE);
        paymentChannel.setSupportRefunded(Boolean.TRUE);
        paymentChannel.setCreateDate(current);
        paymentChannelService.create(paymentChannel);
        //支付方式-即时到账
        initWebPayConfig(current, paymentChannel);
        //支付方式-手机网关支付
        initTradeWapPayConfig(current, paymentChannel);
        return paymentChannel;
    }

    /**
     * 初始化即时到账支付方式
     * @param current
     * @param paymentChannel
     */
    private void initWebPayConfig(Date current, PaymentChannel paymentChannel) {
        PaymentWay webPayWay = new PaymentWay();
        webPayWay.setPaymentChannelId(paymentChannel.getId());
        webPayWay.setName("支付宝即时到账支付");
        webPayWay.setCode(AlipayConfig.WAP_WEB_PAY);
        webPayWay.setCreateDate(current);
        paymentWayService.createPaymentWay(webPayWay);
        webPayWay.setPaymentChannel(paymentChannel);
        //即时到账-支付接口
        initAlipayFastPayConfig(webPayWay);
        //即时到账-支付回调接口
        initAlipayFastPayCallBackConfig(webPayWay);
        //支付宝即时到账退款接口
        initAlipayFastPayRefundConfig(webPayWay);
    }

    /**
     * 初始化手机网关支付方式
     * @param current
     * @param paymentChannel
     */
    private void initTradeWapPayConfig(Date current, PaymentChannel paymentChannel) {
        PaymentWay tradeWapPayWay = new PaymentWay();
        tradeWapPayWay.setPaymentChannelId(paymentChannel.getId());
        tradeWapPayWay.setName("支付宝手机网关支付");
        tradeWapPayWay.setCode(AlipayConfig.WAP_TRADE_PAY);
        tradeWapPayWay.setCreateDate(current);
        paymentWayService.createPaymentWay(tradeWapPayWay);
        tradeWapPayWay.setPaymentChannel(paymentChannel);
        //手机网关-支付接口
        initTradePayConfig(tradeWapPayWay);
        //手机网关-支付回调接口
        initTradePayCallbackConfig(tradeWapPayWay);
        //支付宝手机网关退款接口
        initTradeRefundConfig(tradeWapPayWay);
        //支付宝手机网关查询接口
        initTradeQueryConfig(tradeWapPayWay);
    }

    private void initAlipayFastPayRefundConfig(PaymentWay webPayWay) {
        PaymentInterface fastPayRefund = new PaymentInterface();
        fastPayRefund.setPaymentWayId(webPayWay.getId());
        fastPayRefund.setInterfaceName("支付宝即时到账退款接口");
        fastPayRefund.setInterfaceCode(AlipayConfig.INTERFACE_FASTPAY_REFUND);
        fastPayRefund.setRequestUrl(AlipayConfig.FASTPAY_REQUEST_URL);
        fastPayRefund.setCallbackUrl(AlipayConfig.FASTPAY_REFUND_CALLBACK_URL);
        fastPayRefund.setReturnPageUrl(AlipayConfig.RETURN_PAGE_URL);
        fastPayRefund.setPaymentInterfaceType(PaymentInterfaceType.REFUND);
        fastPayRefund.setHandlerBeanName("alipayFastpayRefundHandler");
        paymentInterfaceService.createPaymentInterface(fastPayRefund);
        fastPayRefund.setPaymentWay(webPayWay);
    }

    private void initAlipayFastPayCallBackConfig(PaymentWay webPayWay) {
        PaymentInterface fastPayCallback = new PaymentInterface();
        fastPayCallback.setPaymentWayId(webPayWay.getId());
        fastPayCallback.setInterfaceName("支付宝即时到账支付回调接口");
        fastPayCallback.setInterfaceCode(AlipayConfig.INTERFACE_FASTPAY_CALLBACK);
        fastPayCallback.setPaymentInterfaceType(PaymentInterfaceType.PAY_NOTIFY);
        fastPayCallback.setHandlerBeanName("alipayCallBackHandler");
        paymentInterfaceService.createPaymentInterface(fastPayCallback);
        fastPayCallback.setPaymentWay(webPayWay);
    }

    private void initAlipayFastPayConfig(PaymentWay webPayWay) {
        PaymentInterface alipayFastPay = new PaymentInterface();
        alipayFastPay.setPaymentWayId(webPayWay.getId());
        alipayFastPay.setInterfaceName("支付宝即时到账支付接口");
        alipayFastPay.setInterfaceCode(AlipayConfig.INTERFACE_FASTPAY);
        alipayFastPay.setRequestUrl(AlipayConfig.FASTPAY_REQUEST_URL);
        alipayFastPay.setCallbackUrl(AlipayConfig.FASTPAY_CALLBACK_URL);
        alipayFastPay.setReturnPageUrl(AlipayConfig.RETURN_PAGE_URL);
        alipayFastPay.setPaymentInterfaceType(PaymentInterfaceType.PAY);
        alipayFastPay.setHandlerBeanName("alipayWebPaymentHandler");
        paymentInterfaceService.createPaymentInterface(alipayFastPay);
        alipayFastPay.setPaymentWay(webPayWay);
    }

    private void initTradeQueryConfig(PaymentWay tradeWapPayWay) {
        PaymentInterface tradeQuery = new PaymentInterface();
        tradeQuery.setPaymentWayId(tradeWapPayWay.getId());
        tradeQuery.setInterfaceName("支付宝手机网关查询接口");
        tradeQuery.setInterfaceCode(AlipayConfig.INTERFACE_TRADE_QUERY);
        tradeQuery.setRequestUrl(AlipayConfig.TRADE_REQUEST_URL);
        tradeQuery.setPaymentInterfaceType(PaymentInterfaceType.QUERY);
        tradeQuery.setHandlerBeanName("alipayPaymentQueryHandler");
        paymentInterfaceService.createPaymentInterface(tradeQuery);
        tradeQuery.setPaymentWay(tradeWapPayWay);
    }

    private void initTradeRefundConfig(PaymentWay tradeWapPayWay) {
        PaymentInterface tradeRefund = new PaymentInterface();
        tradeRefund.setPaymentWayId(tradeWapPayWay.getId());
        tradeRefund.setInterfaceName("支付宝手机网关退款接口");
        tradeRefund.setInterfaceCode(AlipayConfig.INTERFACE_TRADE_REFUND);
        tradeRefund.setRequestUrl(AlipayConfig.TRADE_REQUEST_URL);
        tradeRefund.setPaymentInterfaceType(PaymentInterfaceType.REFUND);
        tradeRefund.setHandlerBeanName("alipayPaymentRefundHandler");
        paymentInterfaceService.createPaymentInterface(tradeRefund);
        tradeRefund.setPaymentWay(tradeWapPayWay);
    }

    private void initTradePayCallbackConfig(PaymentWay tradeWapPayWay) {
        PaymentInterface tradePayCallback = new PaymentInterface();
        tradePayCallback.setPaymentWayId(tradeWapPayWay.getId());
        tradePayCallback.setInterfaceName("支付宝手机网关支付回调接口");
        tradePayCallback.setInterfaceCode(AlipayConfig.INTERFACE_TRADE_CALLBACK);
        tradePayCallback.setPaymentInterfaceType(PaymentInterfaceType.PAY_NOTIFY);
        tradePayCallback.setHandlerBeanName("alipayTradeWapCallBackHandler");
        paymentInterfaceService.createPaymentInterface(tradePayCallback);
        tradePayCallback.setPaymentWay(tradeWapPayWay);
    }

    private void initTradePayConfig(PaymentWay tradeWapPayWay) {
        PaymentInterface tradePay = new PaymentInterface();
        tradePay.setPaymentWayId(tradeWapPayWay.getId());
        tradePay.setInterfaceName("支付宝手机网关支付接口");
        tradePay.setInterfaceCode(AlipayConfig.INTERFACE_TRADE_WAPPAY);
        tradePay.setRequestUrl(AlipayConfig.TRADE_REQUEST_URL);
        tradePay.setCallbackUrl(AlipayConfig.TRADE_CALLBACK_URL);
        tradePay.setReturnPageUrl(AlipayConfig.RETURN_PAGE_URL);
        tradePay.setPaymentInterfaceType(PaymentInterfaceType.PAY);
        tradePay.setHandlerBeanName("alipayTradeWapPaymentHandler");
        paymentInterfaceService.createPaymentInterface(tradePay);
        tradePay.setPaymentWay(tradeWapPayWay);
    }
}
