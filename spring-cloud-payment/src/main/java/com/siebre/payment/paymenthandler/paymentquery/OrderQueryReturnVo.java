package com.siebre.payment.paymenthandler.paymentquery;

import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import org.codehaus.jackson.JsonNode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Huang Tianci
 * 查询报文解析后的模型
 */
public class OrderQueryReturnVo implements Serializable{

    //交易状态
    private PaymentOrderPayStatus tradeState;

    //远程订单金额
    private BigDecimal remoteOrderAmount;

    //远程支付完成时间
    private Date remotePayTime;

    public PaymentOrderPayStatus getTradeState() {
        return tradeState;
    }

    public void setTradeState(PaymentOrderPayStatus tradeState) {
        this.tradeState = tradeState;
    }

    public BigDecimal getRemoteOrderAmount() {
        return remoteOrderAmount;
    }

    public void setRemoteOrderAmount(BigDecimal remoteOrderAmount) {
        this.remoteOrderAmount = remoteOrderAmount;
    }

    public Date getRemotePayTime() {
        return remotePayTime;
    }

    public void setRemotePayTime(Date remotePayTime) {
        this.remotePayTime = remotePayTime;
    }
}
