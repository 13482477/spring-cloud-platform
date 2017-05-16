package com.siebre.payment.serviceinterface.refundapplication;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.entity.refundapplication.RefundApplication;

import java.util.Date;
import java.util.List;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public interface IRefundApplication {
    ServiceResult<List<RefundApplication>> getRefundApplicationByPage(PageInfo pageInfo);

    ServiceResult<RefundApplication> getRefundApplicationByOrderNumber(String orderNumber);

    ServiceResult<RefundApplication> getRefundApplicationByRefundApplicationNumber(String refundApplicationNumber);

    public ServiceResult<List<RefundApplication>> selectRefundList(String orderNumber, String refundNumber,
                                                                   String channelName, RefundApplicationStatus refundStatus,
                                                                   Date startDate, Date endDate, PageInfo pageInfo);

}
