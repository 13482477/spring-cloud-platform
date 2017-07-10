package com.siebre.payment.paymenthandler.paymentquery;

import com.siebre.payment.paymentorder.entity.PaymentOrder;
import org.codehaus.jackson.JsonNode;

import java.util.Map;

public class PaymentQueryResponse {

    private String returnCode;

    private String returnMessage;

    private PaymentOrder localOrder;

    //解析返回报文后的得到的订单数据对象
    private OrderQueryReturnVo queryResult;

    //查询订单交易状态的返回报文
    private String remoteJson;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public PaymentOrder getLocalOrder() {
        return localOrder;
    }

    public void setLocalOrder(PaymentOrder localOrder) {
        this.localOrder = localOrder;
    }

    public OrderQueryReturnVo getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(OrderQueryReturnVo queryResult) {
        this.queryResult = queryResult;
    }

    public String getRemoteJson() {
        return remoteJson;
    }

    public void setRemoteJson(String remoteJson) {
        this.remoteJson = remoteJson;
    }
}
