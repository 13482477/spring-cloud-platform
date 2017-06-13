package com.siebre.payment.paymentcheck.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by meilan on 2017/6/6.
 * 对账总览结果
 */
public class CheckOverviewResult implements Serializable {
    //对账总笔数
    private int checkTotalCount;

    //成功笔数
    private int successCount;

    //失败笔数
    private int failCount;

    //异常笔数
    private int unusualCount;

    //未对账总笔数
    private int notCheckTotalCount;

    //支付信息-订单金额
    private BigDecimal payOrderTotalAmount;

    //支付信息-支付金额
    private BigDecimal payTotalAmount;

    //退款信息-订单金额
    private BigDecimal refundOrderTotalAmount;

    //退款信息-退款金额
    private BigDecimal refundTotalAmount;

    public int getCheckTotalCount() {
        return checkTotalCount;
    }

    public void setCheckTotalCount(int checkTotalCount) {
        this.checkTotalCount = checkTotalCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getUnusualCount() {
        return unusualCount;
    }

    public void setUnusualCount(int unusualCount) {
        this.unusualCount = unusualCount;
    }

    public BigDecimal getPayOrderTotalAmount() {
        return payOrderTotalAmount;
    }

    public void setPayOrderTotalAmount(BigDecimal payOrderTotalAmount) {
        this.payOrderTotalAmount = payOrderTotalAmount;
    }

    public BigDecimal getPayTotalAmount() {
        return payTotalAmount;
    }

    public void setPayTotalAmount(BigDecimal payTotalAmount) {
        this.payTotalAmount = payTotalAmount;
    }

    public BigDecimal getRefundOrderTotalAmount() {
        return refundOrderTotalAmount;
    }

    public void setRefundOrderTotalAmount(BigDecimal refundOrderTotalAmount) {
        this.refundOrderTotalAmount = refundOrderTotalAmount;
    }

    public BigDecimal getRefundTotalAmount() {
        return refundTotalAmount;
    }

    public void setRefundTotalAmount(BigDecimal refundTotalAmount) {
        this.refundTotalAmount = refundTotalAmount;
    }

    public int getNotCheckTotalCount() {
        return notCheckTotalCount;
    }

    public void setNotCheckTotalCount(int notCheckTotalCount) {
        this.notCheckTotalCount = notCheckTotalCount;
    }
}
