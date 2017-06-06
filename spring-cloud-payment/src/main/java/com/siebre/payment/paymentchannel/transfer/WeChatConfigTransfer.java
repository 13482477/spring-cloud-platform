package com.siebre.payment.paymentchannel.transfer;

import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentChannelStatus;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymentchannel.vo.WeChatConfigVo;
import com.siebre.payment.paymenthandler.wechatpay.sdk.WeChatConfig;
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
 *         微信配置信息转换成渠道模型的转换类
 */
@Service("weChatConfigTransfer")
public class WeChatConfigTransfer {

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    private PaymentWayService paymentWayService;

    @Autowired
    private PaymentInterfaceService paymentInterfaceService;

    @Transactional("db")
    public ServiceResult<WeChatConfigVo> transfer(WeChatConfigVo weChatConfigVo) {
        Date current = new Date();

        PaymentChannel paymentChannel = paymentChannelService.queryByChannelCode(WeChatConfig.CHANNEL_CODE).getData();
        if (paymentChannel == null) {
            paymentChannel = initChannel();
        }

        paymentChannel.setMerchantCode(weChatConfigVo.getMerchantCode());
        //操作员账号使用payeeAccount
        paymentChannel.setPayeeAccount(weChatConfigVo.getOpUesrId());
        paymentChannel.setUpdateDate(current);
        paymentChannelService.updateById(paymentChannel);

        PaymentWay sacnWay = paymentWayService.getPaymentWay(WeChatConfig.WAY_SCAN_PAY);
        sacnWay.setAppId(weChatConfigVo.getAppId());
        sacnWay.setSecretKey(weChatConfigVo.getSecretKey());
        //微信的appSecret使用publicKey字段来存储
        sacnWay.setPublicKey(weChatConfigVo.getAppSecret());
        sacnWay.setUpdateDate(current);
        paymentWayService.updatePaymentWay(sacnWay);

        PaymentWay publicWay = paymentWayService.getPaymentWay(WeChatConfig.WAY_PUBLIC_PAY);
        publicWay.setAppId(weChatConfigVo.getAppId());
        publicWay.setSecretKey(weChatConfigVo.getSecretKey());
        //微信的appSecret使用publicKey字段来存储
        publicWay.setPublicKey(weChatConfigVo.getAppSecret());
        publicWay.setUpdateDate(current);
        paymentWayService.updatePaymentWay(publicWay);

        return ServiceResult.<WeChatConfigVo>builder().success(Boolean.TRUE).data(weChatConfigVo).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    public ServiceResult<WeChatConfigVo> transferToVo() {
        WeChatConfigVo weChatConfigVo = new WeChatConfigVo();
        PaymentChannel paymentChannel = paymentChannelService.queryByChannelCode(WeChatConfig.CHANNEL_CODE).getData();
        weChatConfigVo.setMerchantCode(paymentChannel.getMerchantCode());
        weChatConfigVo.setOpUesrId(paymentChannel.getPayeeAccount());
        PaymentWay publicWay = paymentWayService.getPaymentWay(WeChatConfig.WAY_PUBLIC_PAY);
        weChatConfigVo.setAppId(publicWay.getAppId());
        weChatConfigVo.setSecretKey(publicWay.getSecretKey());
        weChatConfigVo.setAppSecret(publicWay.getPublicKey());
        return ServiceResult.<WeChatConfigVo>builder().success(Boolean.TRUE).data(weChatConfigVo).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    private PaymentChannel initChannel() {
        Date current = new Date();
        PaymentChannel paymentChannel = new PaymentChannel();
        paymentChannel.setChannelName("微信支付");
        paymentChannel.setChannelCode(WeChatConfig.CHANNEL_CODE);
        paymentChannel.setStatus(PaymentChannelStatus.ENABLE);
        paymentChannel.setSupportRefunded(Boolean.TRUE);
        paymentChannel.setCreateDate(current);
        paymentChannelService.create(paymentChannel);
        //微信扫码支付
        initScanPayWay(current, paymentChannel);
        //微信公众号支付
        initPublicPayWay(current, paymentChannel);
        return paymentChannel;
    }

    private void initPublicPayWay(Date current, PaymentChannel paymentChannel) {
        PaymentWay paymentWay = new PaymentWay();
        paymentWay.setPaymentChannelId(paymentChannel.getId());
        paymentWay.setName("微信公众号支付");
        paymentWay.setCode(WeChatConfig.WAY_PUBLIC_PAY);
        paymentWay.setCreateDate(current);
        paymentWayService.createPaymentWay(paymentWay);
        paymentWay.setPaymentChannel(paymentChannel);
        //微信公众号支付接口
        initPublicPayInterface(paymentWay);
        //微信公众号支付回调接口
        initPublicPayCallbackInterface(paymentWay);
        //微信订单查询接口
        initQueryInterface(paymentWay);
        //微信退款接口
        initRefundInterface(paymentWay);
    }

    private void initRefundInterface(PaymentWay paymentWay) {
        PaymentInterface paymentInterface = new PaymentInterface();
        paymentInterface.setPaymentWayId(paymentWay.getId());
        paymentInterface.setInterfaceName("微信退款接口");
        paymentInterface.setInterfaceCode(WeChatConfig.INTERFACE_REFUND);
        paymentInterface.setRequestUrl(WeChatConfig.REFUND_URL);
        paymentInterface.setPaymentInterfaceType(PaymentInterfaceType.REFUND);
        paymentInterface.setHandlerBeanName("weChatPaymentRefundHandler");
        paymentInterfaceService.createPaymentInterface(paymentInterface);
        paymentInterface.setPaymentWay(paymentWay);
    }

    private void initPublicPayCallbackInterface(PaymentWay paymentWay) {
        PaymentInterface paymentInterface = new PaymentInterface();
        paymentInterface.setPaymentWayId(paymentWay.getId());
        paymentInterface.setInterfaceName("微信公众号支付回调接口");
        paymentInterface.setInterfaceCode(WeChatConfig.INTERFACE_PUBLIC_PAY_CALLBACK);
        paymentInterface.setPaymentInterfaceType(PaymentInterfaceType.PAY_NOTIFY);
        paymentInterface.setHandlerBeanName("weChatPublicCallBackHandler");
        paymentInterfaceService.createPaymentInterface(paymentInterface);
        paymentInterface.setPaymentWay(paymentWay);
    }

    private void initPublicPayInterface(PaymentWay paymentWay) {
        PaymentInterface paymentInterface = new PaymentInterface();
        paymentInterface.setPaymentWayId(paymentWay.getId());
        paymentInterface.setInterfaceName("微信公众号支付接口");
        paymentInterface.setInterfaceCode(WeChatConfig.INTERFACE_PUBLIC_PAY);
        paymentInterface.setRequestUrl(WeChatConfig.PAY_REQUEST_URL);
        paymentInterface.setCallbackUrl(WeChatConfig.PUBLIC_CALLBACK_URL);
        paymentInterface.setPaymentInterfaceType(PaymentInterfaceType.PAY);
        paymentInterface.setHandlerBeanName("weChatPublicPaymentHandler");
        paymentInterfaceService.createPaymentInterface(paymentInterface);
        paymentInterface.setPaymentWay(paymentWay);
    }

    private void initScanPayWay(Date current, PaymentChannel paymentChannel) {
        PaymentWay paymentWay = new PaymentWay();
        paymentWay.setPaymentChannelId(paymentChannel.getId());
        paymentWay.setName("微信扫码支付");
        paymentWay.setCode(WeChatConfig.WAY_SCAN_PAY);
        paymentWay.setCreateDate(current);
        paymentWayService.createPaymentWay(paymentWay);
        paymentWay.setPaymentChannel(paymentChannel);
        //微信扫码支付支付接口
        initScanPayInterface(paymentWay);
        //微信扫码支付回调接口
        initScanPayCallBackInterface(paymentWay);
        //微信订单查询接口
        initQueryInterface(paymentWay);
    }

    private void initQueryInterface(PaymentWay paymentWay) {
        PaymentInterface paymentInterface = new PaymentInterface();
        paymentInterface.setPaymentWayId(paymentWay.getId());
        paymentInterface.setInterfaceName("微信订单查询接口");
        paymentInterface.setInterfaceCode(WeChatConfig.INTERFACE_ORDER_QUERY);
        paymentInterface.setRequestUrl(WeChatConfig.QUERY_REQUEST_URL);
        paymentInterface.setPaymentInterfaceType(PaymentInterfaceType.QUERY);
        paymentInterface.setHandlerBeanName("weChatQueryHandler");
        paymentInterfaceService.createPaymentInterface(paymentInterface);
        paymentInterface.setPaymentWay(paymentWay);
    }

    private void initScanPayCallBackInterface(PaymentWay paymentWay) {
        PaymentInterface paymentInterface = new PaymentInterface();
        paymentInterface.setPaymentWayId(paymentWay.getId());
        paymentInterface.setInterfaceName("微信扫码支付回调接口");
        paymentInterface.setInterfaceCode(WeChatConfig.INTERFACE_SACN_PAY_CALLBACK);
        paymentInterface.setPaymentInterfaceType(PaymentInterfaceType.PAY_NOTIFY);
        paymentInterface.setHandlerBeanName("weChatCallBackHandler");
        paymentInterfaceService.createPaymentInterface(paymentInterface);
        paymentInterface.setPaymentWay(paymentWay);
    }

    private void initScanPayInterface(PaymentWay paymentWay) {
        PaymentInterface paymentInterface = new PaymentInterface();
        paymentInterface.setPaymentWayId(paymentWay.getId());
        paymentInterface.setInterfaceName("微信扫码支付接口");
        paymentInterface.setInterfaceCode(WeChatConfig.INTERFACE_SCAN_PAY);
        paymentInterface.setRequestUrl(WeChatConfig.PAY_REQUEST_URL);
        paymentInterface.setCallbackUrl(WeChatConfig.SCAN_CALLBACK_URL);
        paymentInterface.setReturnPageUrl(WeChatConfig.RETURN_PAGE_URL);
        paymentInterface.setPaymentInterfaceType(PaymentInterfaceType.PAY);
        paymentInterface.setHandlerBeanName("weChatScanPaymentHandler");
        paymentInterfaceService.createPaymentInterface(paymentInterface);
        paymentInterface.setPaymentWay(paymentWay);
    }

}
