package com.siebre.payment.paymentgateway.vo;

import com.siebre.payment.paymentaccount.entity.BankAccount;
import com.siebre.payment.paymentaccount.entity.WeChatAccount;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Huang Tianci
 *         统一支付接口order模型
 */
public class UnifiedPayOrder implements Serializable {

    @ApiModelProperty(value = "支付方式代码", required = true)
    private String paymentWayCode;

    @ApiModelProperty(value = "支付金额", required = true)
    private BigDecimal amount;

    @ApiModelProperty(value = "支付账户(所有账户字段中，必须要传一个，且只能传一个)", required = false)
    private BankAccount bankAccount;

    @ApiModelProperty(value = "支付账户(所有账户字段中，必须要传一个，且只能传一个)", required = false)
    private WeChatAccount weChatAccount;

    @ApiModelProperty(value = "货币种类", required = false)
    private String currency;

    @ApiModelProperty(value = "保单信息", required = true)
    private List<UnifiedPayItem> items;

    @ApiModelProperty(value = "提示信息", required = false)
    private String summary;

    public String getPaymentWayCode() {
        return paymentWayCode;
    }

    public void setPaymentWayCode(String paymentWayCode) {
        this.paymentWayCode = paymentWayCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public WeChatAccount getWeChatAccount() {
        return weChatAccount;
    }

    public void setWeChatAccount(WeChatAccount weChatAccount) {
        this.weChatAccount = weChatAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<UnifiedPayItem> getItems() {
        return items;
    }

    public void setItems(List<UnifiedPayItem> items) {
        this.items = items;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
