package com.siebre.payment.refundapplication.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentOrderRefundStatus;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import com.siebre.payment.paymentorder.vo.OrderQueryParamsVo;
import com.siebre.payment.paymentorder.vo.Refund;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.refundapplication.mapper.RefundApplicationMapper;
import com.siebre.payment.serialnumber.mapper.SerialNumberMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service
public class RefundApplicationService {

    @Autowired
    private RefundApplicationMapper refundApplicationMapper;

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Autowired
    private SerialNumberMapper serialNumberMapper;

    public ServiceResult<List<RefundApplication>> selectRefundList(String orderNumber, String refundNumber,
                                                                   List<String> channelCodeList, List<PaymentOrderRefundStatus> refundStatusList,
                                                                   Date startDate, Date endDate, PageInfo pageInfo) {
        List<RefundApplication> list = refundApplicationMapper.selectRefundList(orderNumber, refundNumber, channelCodeList, refundStatusList, startDate, endDate, pageInfo);
        ServiceResult<List<RefundApplication>> result = new ServiceResult<>();
        result.setData(list);
        result.setPageInfo(pageInfo);
        return result;
    }

    public ServiceResult<List<RefundApplication>> getRefundApplicationByPage(PageInfo pageInfo) {
        List<RefundApplication> list = refundApplicationMapper.selectByPage(pageInfo);
        ServiceResult<List<RefundApplication>> result = new ServiceResult<>();
        result.setData(list);
        result.setPageInfo(pageInfo);
        return result;
    }

    public ServiceResult<RefundApplication> getRefundApplicationByOrderNumber(String orderNumber) {
        ServiceResult<RefundApplication> result = new ServiceResult<>();
        result.setData(refundApplicationMapper.selectByBusinessNumber(orderNumber, null));
        return result;
    }

    public ServiceResult<RefundApplication> getRefundApplicationByRefundApplicationNumber(String refundApplicationNumber) {
        ServiceResult<RefundApplication> result = new ServiceResult<>();
        result.setData(refundApplicationMapper.selectByBusinessNumber(null, refundApplicationNumber));
        return result;
    }

    @Transactional("db")
    public RefundApplication createRefundApplication(RefundApplication refundApplication) {
        PaymentOrder order = paymentOrderMapper.selectByOrderNumber(refundApplication.getOrderNumber());
        if (order == null) {
            refundApplication.setStatus(RefundApplicationStatus.FAILED);
            refundApplication.setResponse("该订单不存在，请检查");
        }
        if (!order.getStatus().equals(PaymentOrderPayStatus.PAID)) {
            refundApplication.setStatus(RefundApplicationStatus.FAILED);
            refundApplication.setResponse("该订单状态为" + order.getStatus().getDescription() + "，不能申请退款");
        }

        BigDecimal totalAmount = order.getTotalPremium();
        BigDecimal refundedAmount = order.getRefundAmount() == null ? BigDecimal.ZERO : order.getRefundAmount();

        if (refundedAmount.add(refundApplication.getRefundAmount()).compareTo(totalAmount) <= 0) {
            //查询数据库，该订单是否已存在对应的退款申请，如果已存在，使用已存在的退款申请
            RefundApplication refundApplication2 = refundApplicationMapper.selectByBusinessNumber(order.getOrderNumber(), null);
            if (refundApplication2 != null) {
                return refundApplication2;
            } else {
                refundApplication.setCreateDate(new Date());
                refundApplicationMapper.insertSelective(refundApplication);
            }
        } else {
            refundApplication.setStatus(RefundApplicationStatus.FAILED);
            refundApplication.setResponse("退款金额超限");
        }
        return refundApplication;
    }

    public List<Refund> qeuryRefundByPage(OrderQueryParamsVo paramsVo, PageInfo page) {
        String orderNumber = paramsVo.getOrderNumber();
        String refundNumber = paramsVo.getRefundNumber();
        Date startDate = paramsVo.getStartDate();
        Date endDate = paramsVo.getEndDate();
        ServiceResult<List<RefundApplication>> refundsListResult = this.selectRefundList(orderNumber, refundNumber, paramsVo.getChannelCodeList(),
                paramsVo.getRefundStatusList(), startDate, endDate, page);
        List<RefundApplication> refundsList = refundsListResult.getData();
        page.setTotalPage(refundsListResult.getPageInfo().getTotalPage());
        List<Refund> result = new ArrayList<>();
        for (RefundApplication refundApp : refundsList) {
            Refund refund = new Refund();
            refund.setOrderNumber(refundApp.getOrderNumber());
            refund.setRefundNumber(refundApp.getRefundApplicationNumber());
            refund.setChannelName(refundApp.getPaymentOrder().getPaymentChannel().getChannelName());
            refund.setOrderAmount(refundApp.getPaymentOrder().getTotalPremium().toString());
            refund.setRefundAmount(refundApp.getRefundAmount().toString());
            refund.setRefundStatus(refundApp.getStatus().getDescription());
            if(RefundApplicationStatus.SUCCESS.equals(refundApp.getStatus())){
                refund.setOrderRefundStatus(refundApp.getPaymentOrder().getStatus().getDescription());
            }
            if(refundApp.getCreateDate() != null) {
                String dateStr = DateFormatUtils.format(refundApp.getCreateDate(), "yyyy-MM-dd HH:mm:ss");
                refund.setCreateDate(dateStr);
            }
            result.add(refund);
        }
        return result;
    }


}
