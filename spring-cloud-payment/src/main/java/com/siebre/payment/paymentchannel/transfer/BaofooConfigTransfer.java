package com.siebre.payment.paymentchannel.transfer;

import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentChannelStatus;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymentchannel.vo.BaofooConfigVo;
import com.siebre.payment.paymenthandler.baofoo.sdk.BaofooConfig;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentinterface.service.PaymentInterfaceService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by meilan on 2017/6/2.
 * 宝付配置信息--渠道模型 转换类
 */
@Service("baofooConfigTransfer")
public class BaofooConfigTransfer {
    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    private PaymentWayService paymentWayService;

    @Autowired
    private PaymentInterfaceService paymentInterfaceService;

    @Transactional("db")
    public ServiceResult<BaofooConfigVo> transfer(BaofooConfigVo baofooConfigVo) {
        Date current = new Date();
        PaymentChannel baofooPayChannel = paymentChannelService.queryByChannelCode(BaofooConfig.CHANNEL_CODE).getData();
        if (baofooPayChannel == null) {
            baofooPayChannel = initBaofooChannel();
        }
        baofooPayChannel.setMerchantCode(baofooConfigVo.getMerchantCode());
        baofooPayChannel.setTerminalId(baofooConfigVo.getTerminalId());
        baofooPayChannel.setUpdateDate(current);
        paymentChannelService.updateById(baofooPayChannel);

        PaymentWay fastPayWay = paymentWayService.getPaymentWay(BaofooConfig.BAOFOO_FAST_PAY);
        fastPayWay.setSecretKey(baofooConfigVo.getSecretKey());
        fastPayWay.setUpdateDate(current);
        paymentWayService.updatePaymentWay(fastPayWay);

        return ServiceResult.<BaofooConfigVo>builder().success(Boolean.TRUE).data(baofooConfigVo).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    public ServiceResult<BaofooConfigVo> detailTransfer() {
        BaofooConfigVo baofooConfigVo = new BaofooConfigVo();
        PaymentChannel baofooPayChannel = paymentChannelService.queryByChannelCode(BaofooConfig.CHANNEL_CODE).getData();
        baofooConfigVo.setMerchantCode(baofooPayChannel.getMerchantCode());
        baofooConfigVo.setTerminalId(baofooPayChannel.getTerminalId());

        PaymentWay fastPayWay = paymentWayService.getPaymentWay(BaofooConfig.BAOFOO_FAST_PAY);
        baofooConfigVo.setSecretKey(fastPayWay.getSecretKey());

        return ServiceResult.<BaofooConfigVo>builder().success(Boolean.TRUE).data(baofooConfigVo).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    /**
     * 初始化宝付渠道
     *
     * @return
     */
    @Transactional("db")
    private PaymentChannel initBaofooChannel() {
        Date current = new Date();

        PaymentChannel paymentChannel = new PaymentChannel();
        paymentChannel.setChannelName("宝付支付");
        paymentChannel.setChannelCode(BaofooConfig.CHANNEL_CODE);
        paymentChannel.setStatus(PaymentChannelStatus.ENABLE);
        paymentChannel.setSupportRefunded(Boolean.FALSE);//暂不支持退款
        paymentChannel.setCreateDate(current);
        paymentChannelService.create(paymentChannel);

        //宝付-快捷支付方式
        initFastpayConfig(current, paymentChannel);

        //宝付-代扣支付

        return paymentChannel;
    }

    /**
     * 初始化宝付快捷支付支付方式
     *
     * @param current
     * @param paymentChannel
     */
    private void initFastpayConfig(Date current, PaymentChannel paymentChannel) {
        PaymentWay fastPayWay = new PaymentWay();
        fastPayWay.setPaymentChannelId(paymentChannel.getId());
        fastPayWay.setName("宝付快捷支付");
        fastPayWay.setCode(BaofooConfig.BAOFOO_FAST_PAY);
        fastPayWay.setCreateDate(current);
        paymentWayService.createPaymentWay(fastPayWay);
        fastPayWay.setPaymentChannel(paymentChannel);

        //宝付-快捷支付预支付接口
        initFastpayInterfacePay(fastPayWay);

        //退款，查询
    }

    private void initFastpayInterfacePay(PaymentWay fastPayWay) {
        PaymentInterface fastPayInter = new PaymentInterface();
        fastPayInter.setPaymentWayId(fastPayWay.getId());
        fastPayInter.setInterfaceName("宝付快捷支付预支付接口");
        fastPayInter.setInterfaceCode(BaofooConfig.BAOFOO_FASTPAY_PREPAY);
        fastPayInter.setRequestUrl(BaofooConfig.BAOFOO_FASTPAY_URL);
        fastPayInter.setPaymentInterfaceType(PaymentInterfaceType.PAY);
        fastPayInter.setHandlerBeanName("baofooQuickPaymentHandler");
        paymentInterfaceService.createPaymentInterface(fastPayInter);
        fastPayInter.setPaymentWay(fastPayWay);

    }

}
