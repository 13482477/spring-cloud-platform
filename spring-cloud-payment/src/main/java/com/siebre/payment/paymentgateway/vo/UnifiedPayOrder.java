package com.siebre.payment.paymentgateway.vo;

import com.siebre.payment.paymentaccount.entity.BankAccount;
import com.siebre.payment.paymentaccount.entity.WeChatAccount;
import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Huang Tianci
 * 统一支付接口order模型
 */
public class UnifiedPayOrder {

    @ApiModelProperty(value = "支付方法", required = true)
    private String paymentMethod;

    @ApiModelProperty(value = "支付渠道", required = true)
    private String paymentProvider;

    private BigDecimal amount;

    private BankAccount bankAccount;

    private WeChatAccount weChatAccount;

    private String currency;

    private List<PaymentOrderItem> items;

    private String summary;

}
