package com.siebre.payment.paymentgateway.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 统一支付接口请求模型 v2版本，v2版本接口开发完成之后，清除v1版本的请求模型，并重构请求模型名称
 */
public class UnifiedPayRequestV2 implements Serializable {

    @ApiModelProperty(value = "由前端维护，唯一标识一次支付请求", required = true)
    private String messageId;

    @ApiModelProperty(value = "支付回调地址", required = false)
    private String notificationUrl;

    private UnifiedPayOrder paymentOrder;

}
