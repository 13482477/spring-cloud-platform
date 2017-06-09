package com.siebre.payment.paymentorder.vo;

import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentOrderRefundStatus;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Huang Tianci
 *         订单查询参数
 */
public class OrderQueryParamsVo implements Serializable {

    @ApiModelProperty(value = "显示记录数", required = true)
    private int showCount;

    @ApiModelProperty(value = "当前页", required = true)
    private int currentPage;

    @ApiModelProperty(value = "订单编号", required = false)
    private String orderNumber;

    @ApiModelProperty(value = "退款单号", required = false)
    private String refundNumber;

    @ApiModelProperty(value = "支付渠道代码", required = false)
    private List<String> channelCodeList = new ArrayList<>();

    @ApiModelProperty(value = "订单支付状态", required = false)
    private List<PaymentOrderPayStatus> payStatusList = new ArrayList<>();

    @ApiModelProperty(value = "订单退款状态", required = false)
    private List<PaymentOrderRefundStatus> refundStatusList = new ArrayList<>();

    @ApiModelProperty(value = "开始日期", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @ApiModelProperty(value = "结束日期", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(String refundNumber) {
        this.refundNumber = refundNumber;
    }

    public List<String> getChannelCodeList() {
        return channelCodeList;
    }

    public void setChannelCodeList(List<String> channelCodeList) {
        this.channelCodeList = channelCodeList;
    }

    public List<PaymentOrderPayStatus> getPayStatusList() {
        return payStatusList;
    }

    public void setPayStatusList(List<PaymentOrderPayStatus> payStatusList) {
        this.payStatusList = payStatusList;
    }

    public List<PaymentOrderRefundStatus> getRefundStatusList() {
        return refundStatusList;
    }

    public void setRefundStatusList(List<PaymentOrderRefundStatus> refundStatusList) {
        this.refundStatusList = refundStatusList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
