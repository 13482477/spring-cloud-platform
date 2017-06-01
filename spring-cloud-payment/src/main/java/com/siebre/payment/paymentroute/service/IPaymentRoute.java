package com.siebre.payment.paymentroute.service;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public interface IPaymentRoute<I,O> {
    O route(I i);
}
