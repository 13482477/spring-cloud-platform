package com.siebre.payment.hostconfig.service;

import com.siebre.payment.hostconfig.entity.PaymentHostConfig;
import com.siebre.payment.hostconfig.mapper.PaymentHostConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tianci.huang on 2017/6/20.
 */
@Service("paymentHostConfigService")
public class PaymentHostConfigService {

    @Autowired
    private PaymentHostConfigMapper paymentHostConfigMapper;

    private synchronized PaymentHostConfig createPaymentHostConfig(){
        PaymentHostConfig paymentHostConfig = new PaymentHostConfig();
        paymentHostConfigMapper.insert(paymentHostConfig);
        return paymentHostConfig;
    }

    public PaymentHostConfig setPaymentHost(String paymentHost) {
        PaymentHostConfig paymentHostConfig = null;
        List<PaymentHostConfig> list = paymentHostConfigMapper.select();
        if(list.size() == 0) {
            paymentHostConfig = createPaymentHostConfig();
        } else {
            paymentHostConfig = list.get(0);
        }
        paymentHostConfig.setPaymentHost(paymentHost);
        paymentHostConfigMapper.updateByPrimaryKeySelective(paymentHostConfig);
        return paymentHostConfig;
    }

    public PaymentHostConfig setFrontHost(String frontHost) {
        PaymentHostConfig paymentHostConfig = null;
        List<PaymentHostConfig> list = paymentHostConfigMapper.select();
        if(list.size() == 0) {
            paymentHostConfig = createPaymentHostConfig();
        } else {
            paymentHostConfig = list.get(0);
        }
        paymentHostConfig.setFrontHost(frontHost);
        paymentHostConfigMapper.updateByPrimaryKeySelective(paymentHostConfig);
        return paymentHostConfig;
    }

    public String getPaymentHost() {
        List<PaymentHostConfig> list = paymentHostConfigMapper.select();
        if(list.size() == 0) {
            return null;
        } else {
            return list.get(0).getPaymentHost();
        }
    }

    public String getFrontHost() {
        List<PaymentHostConfig> list = paymentHostConfigMapper.select();
        if(list.size() == 0) {
            return null;
        } else {
            return list.get(0).getFrontHost();
        }
    }
}
