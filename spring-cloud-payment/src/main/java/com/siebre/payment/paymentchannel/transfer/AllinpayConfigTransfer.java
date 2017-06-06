package com.siebre.payment.paymentchannel.transfer;


import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentChannelStatus;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymentchannel.vo.AllinPayConfigVo;
import com.siebre.payment.paymenthandler.allinpay.sdk.AllinpayConstants;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentinterface.service.PaymentInterfaceService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by meilan on 2017/6/1.
 * 通联配置信息--渠道模型 转换类
 */
@Service("allinpayConfigTransfer")
public class AllinpayConfigTransfer {
    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    private PaymentWayService paymentWayService;

    @Autowired
    private PaymentInterfaceService paymentInterfaceService;

    @Transactional("db")
    public ServiceResult<AllinPayConfigVo> transfer(AllinPayConfigVo allinPayConfigVo) {
        Date current = new Date();
        PaymentChannel allinPayChannel = paymentChannelService.queryByChannelCode(AllinpayConstants.CHANNEL_CODE).getData();
        if(allinPayChannel == null) {
            allinPayChannel = initAllinpayChannel();
        }
        allinPayChannel.setMerchantCode(allinPayConfigVo.getMerchantCode());
        allinPayChannel.setMerchantName(allinPayConfigVo.getMerchantName());
        allinPayChannel.setMerchantPwd(allinPayConfigVo.getMerchantPwd());
        allinPayChannel.setUpdateDate(current);
        paymentChannelService.updateById(allinPayChannel);

        PaymentWay allinPayWay = paymentWayService.getPaymentWay(AllinpayConstants.ALLIN_REALTIME_PAY);
        allinPayWay.setSecretKey(allinPayConfigVo.getSecretKey());
        allinPayWay.setUpdateDate(current);
        paymentWayService.updatePaymentWay(allinPayWay);

        return ServiceResult.<AllinPayConfigVo>builder().success(Boolean.TRUE).data(allinPayConfigVo).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    @Transactional("db")
    public ServiceResult<AllinPayConfigVo> detailTransfer() {
        AllinPayConfigVo allinPayConfigVo = new AllinPayConfigVo();
        PaymentChannel allinPayChannel = paymentChannelService.queryByChannelCode(AllinpayConstants.CHANNEL_CODE).getData();
        allinPayConfigVo.setMerchantCode(allinPayChannel.getMerchantCode());
        allinPayConfigVo.setMerchantName(allinPayChannel.getMerchantName());
        allinPayConfigVo.setMerchantPwd(allinPayChannel.getMerchantPwd());
        PaymentWay allinPayWay = paymentWayService.getPaymentWay(AllinpayConstants.ALLIN_REALTIME_PAY);
        allinPayConfigVo.setSecretKey(allinPayWay.getSecretKey());

        return ServiceResult.<AllinPayConfigVo>builder().success(Boolean.TRUE).data(allinPayConfigVo).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    /**
     * 初始化通联渠道
     * @return
     */
    @Transactional("db")
    private PaymentChannel initAllinpayChannel() {
        Date current = new Date();

        PaymentChannel paymentChannel = new PaymentChannel();
        paymentChannel.setChannelName("通联支付");
        paymentChannel.setChannelCode(AllinpayConstants.CHANNEL_CODE);
        paymentChannel.setStatus(PaymentChannelStatus.ENABLE);
        paymentChannel.setSupportRefunded(Boolean.TRUE);
        paymentChannel.setCreateDate(current);
        paymentChannelService.create(paymentChannel);

        //初始化通联支付方式
        initAllinpayWay(current,paymentChannel);

        return paymentChannel;
    }

    /**
     * 初始化通联支付方式
     * @param current
     * @param paymentChannel
     */
    private void initAllinpayWay(Date current, PaymentChannel paymentChannel) {
        PaymentWay paymentWay = new PaymentWay();
        paymentWay.setPaymentChannelId(paymentChannel.getId());
        paymentWay.setName("通联实时代扣支付");
        paymentWay.setCode(AllinpayConstants.ALLIN_REALTIME_PAY);
        paymentWay.setCreateDate(current);
        paymentWayService.createPaymentWay(paymentWay);
        paymentWay.setPaymentChannel(paymentChannel);

        //初始化支付接口
        initAllinpayInterfacePay(paymentWay);

        //初始化退款接口
        initAllinpayInterfaceRefund(paymentWay);

        //初始化查询接口
        initAllinpayInterfaceQuery(paymentWay);
    }

    /**
     * 初始化支付接口
     * @param paymentWay
     */
    private void initAllinpayInterfacePay(PaymentWay paymentWay) {
        PaymentInterface payInterface = new PaymentInterface();
        payInterface.setPaymentWayId(paymentWay.getId());
        payInterface.setInterfaceName("通联实时代扣支付接口");
        payInterface.setInterfaceCode(AllinpayConstants.ALLIN_ACP_PAYMENT);
        payInterface.setRequestUrl(AllinpayConstants.ALLIN_URL);
        payInterface.setPaymentInterfaceType(PaymentInterfaceType.PAY);
        payInterface.setHandlerBeanName("allinPayRealTimeHandler");
        paymentInterfaceService.createPaymentInterface(payInterface);
        payInterface.setPaymentWay(paymentWay);
    }

    /**
     *初始化退款接口
     * @param paymentWay
     */
    private void initAllinpayInterfaceRefund(PaymentWay paymentWay) {
        PaymentInterface refundInterface = new PaymentInterface();
        refundInterface.setPaymentWayId(paymentWay.getId());
        refundInterface.setInterfaceName("通联实时代扣退款接口");
        refundInterface.setInterfaceCode(AllinpayConstants.ALLIN_ACP_REFUND);
        refundInterface.setRequestUrl(AllinpayConstants.ALLIN_URL);
        refundInterface.setPaymentInterfaceType(PaymentInterfaceType.REFUND);
        refundInterface.setHandlerBeanName("allinPayRealTimeRefundHandler");
        paymentInterfaceService.createPaymentInterface(refundInterface);
        refundInterface.setPaymentWay(paymentWay);
    }

    /**
     * 初始化查询接口
     * @param paymentWay
     */
    private void initAllinpayInterfaceQuery(PaymentWay paymentWay) {
        PaymentInterface queryInterface = new PaymentInterface();
        queryInterface.setPaymentWayId(paymentWay.getId());
        queryInterface.setInterfaceName("通联实时代扣查询接口");
        queryInterface.setInterfaceCode(AllinpayConstants.ALLIN_ACP_QUERY);
        queryInterface.setRequestUrl(AllinpayConstants.ALLIN_URL);
        queryInterface.setPaymentInterfaceType(PaymentInterfaceType.QUERY);
        queryInterface.setHandlerBeanName("allinPayQueryHandler");
        paymentInterfaceService.createPaymentInterface(queryInterface);
        queryInterface.setPaymentWay(paymentWay);
    }

}
