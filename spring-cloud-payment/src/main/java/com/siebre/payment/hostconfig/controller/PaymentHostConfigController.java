package com.siebre.payment.hostconfig.controller;

import com.siebre.payment.hostconfig.entity.PaymentHostConfig;
import com.siebre.payment.hostconfig.service.PaymentHostConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tianci.huang on 2017/6/20.
 */
@RestController
public class PaymentHostConfigController {

    @Autowired
    private PaymentHostConfigService paymentHostConfigService;

    @RequestMapping(value = "/hostConfig/paymentHost", method = RequestMethod.PUT)
    public PaymentHostConfig paymentHost(@RequestParam String paymentHost) {
        return paymentHostConfigService.setPaymentHost(paymentHost);
    }

    @RequestMapping(value = "/hostConfig/frontHost", method = RequestMethod.PUT)
    public PaymentHostConfig frontHost(@RequestParam String frontHost) {
        return paymentHostConfigService.setFrontHost(frontHost);
    }
}
