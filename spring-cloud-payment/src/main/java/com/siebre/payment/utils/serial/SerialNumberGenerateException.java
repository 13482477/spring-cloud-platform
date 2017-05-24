package com.siebre.payment.utils.serial;

/**
 * Created by AdamTang on 2017/3/30.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class SerialNumberGenerateException extends RuntimeException {
    public SerialNumberGenerateException(String msg) {
        super(msg);
    }

    public SerialNumberGenerateException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
