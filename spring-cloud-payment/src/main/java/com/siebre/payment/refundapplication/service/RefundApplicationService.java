package com.siebre.payment.refundapplication.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentOrderLockStatus;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymentgateway.vo.RefundRequest;
import com.siebre.payment.paymentgateway.vo.RefundResponse;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymentorder.vo.OrderQueryParamsVo;
import com.siebre.payment.paymentorder.vo.Refund;
import com.siebre.payment.paymentroute.service.PaymentRefundRouteService;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.refundapplication.mapper.RefundApplicationMapper;
import com.siebre.payment.serialnumber.mapper.SerialNumberMapper;
import org.apache.commons.lang3.StringUtils;
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

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PaymentTransactionService.class);

    @Autowired
    private RefundApplicationMapper refundApplicationMapper;

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private RefundApplicationService refundApplicationService;

    @Autowired
    private PaymentRefundRouteService paymentRefundRouteService;

    @Autowired
    private SerialNumberMapper serialNumberMapper;

    public ServiceResult<List<RefundApplication>> selectRefundList(String orderNumber, String refundNumber,
                                                                   List<String> channelCodeList,
                                                                   Date startDate, Date endDate, PageInfo pageInfo) {
        List<RefundApplication> list = refundApplicationMapper.selectRefundList(orderNumber, refundNumber, channelCodeList, startDate, endDate, pageInfo);
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
    public void doRefund(RefundRequest refundRequest, RefundResponse refundResponse) {
        if (StringUtils.isBlank(refundRequest.getOrderNumber())) {
            logger.info("订单编号为空", refundRequest.getOrderNumber());
            refundResponse.setReturnCode(ReturnCode.FAIL.getDescription());
            refundResponse.setReturnMessage("订单编号为空");
            return;
        }
        //判断订单是否锁定
        PaymentOrder paymentOrder = paymentOrderService.queryPaymentOrder(refundRequest.getOrderNumber());
        if (PaymentOrderLockStatus.LOCK.equals(paymentOrder.getLockStatus())) {
            logger.info("订单:{} 被锁定，不能退款", paymentOrder.getOrderNumber());
            refundResponse.setReturnCode(ReturnCode.FAIL.getDescription());
            refundResponse.setReturnMessage("订单被锁定，不能退款");
            return;
        }
        //订单只有在支付成功或者部分退款状态下才可以有退款操作
        if (!(PaymentOrderPayStatus.PAID.equals(paymentOrder.getStatus()) || PaymentOrderPayStatus.PART_REFUND.equals(paymentOrder.getStatus()))) {
            logger.info("订单:{} 状态为:{}，不能退款", paymentOrder.getOrderNumber(), paymentOrder.getStatus().getDescription());
            refundResponse.setReturnCode(ReturnCode.FAIL.getDescription());
            refundResponse.setResponse("该订单状态为" + paymentOrder.getStatus().getDescription() + "，不能申请退款");
            return;
        }
        //判断金额是否超限
        BigDecimal refundedAmount = paymentOrder.getRefundAmount() == null ? BigDecimal.ZERO : paymentOrder.getRefundAmount();
        if (refundedAmount.add(refundRequest.getRefundAmount()).compareTo(paymentOrder.getAmount()) > 0) {
            logger.info("订单{}退款金额超限!订单金额：{}, 已退款金额：{}, 本次申请退款金额：{}", paymentOrder.getOrderNumber(), paymentOrder.getAmount(), paymentOrder.getRefundAmount(), refundRequest.getRefundAmount());
            refundResponse.setReturnCode(ReturnCode.FAIL.getDescription());
            refundResponse.setResponse("退款金额超限!订单金额：" + paymentOrder.getAmount() + ", 已退款金额：" + paymentOrder.getRefundAmount() + ", 本次申请退款金额：" + refundRequest.getRefundAmount());
            return;
        }

        RefundApplication application;
        RefundApplication isExist = null;
        //判断该订单是否有处于申请退款状态的refundApplication
        List<RefundApplication> refundApplications = refundApplicationMapper.selectByOrderNumberAndStatus(paymentOrder.getOrderNumber(), RefundApplicationStatus.APPLICATION);
        if (refundApplications.size() > 0) {
            //一个订单处于申请退款状态下的refundApplication最多只能有一个,
            RefundApplication temp = refundApplications.get(0);
            // 如果已存在数据库中的refundApplication的messageId与本次请求相同，那么使用数据库中已存在的refundApplication
            // 否则返回错误信息，该订单已存在一笔退款申请
            if (temp.getMessageId().equals(refundRequest.getMessageId())) {
                isExist = temp;
            } else {
                logger.info("订单:{} 已存在一笔退款申请, 退款申请单号为：{},messageId为：{}。本次请求的messageId为：{}", paymentOrder.getOrderNumber(), temp.getRefundApplicationNumber(),
                        temp.getMessageId(), refundRequest.getMessageId());
                refundResponse.setReturnCode(ReturnCode.FAIL.getDescription());
                refundResponse.setResponse("该订单已存在一笔退款申请, 退款申请单号为：" + temp.getRefundApplicationNumber() + ", messageId为：" + temp.getMessageId());
                return;
            }
        }

        //判断是否重复提交
        if (isExist == null) {
            isExist = refundApplicationMapper.selectByMessageId(refundRequest.getMessageId());
        }

        if (isExist != null) {
            application = isExist;
        } else {
            // 创建RefundApplication
            application = new RefundApplication();
            application.setStatus(RefundApplicationStatus.APPLICATION);
            application.setRequest(refundRequest.getRefundReason());
            application.setOrderNumber(refundRequest.getOrderNumber());
            application.setRefundAmount(refundRequest.getRefundAmount());
            application.setMessageId(refundRequest.getMessageId());
            application.setNotificationUrl(refundRequest.getNotificationUrl());
            application.setCreateDate(new Date());
            refundApplicationMapper.insertSelective(application);
            logger.info("为订单：{}创建一条新的退款申请, 退款单号为：{}", paymentOrder.getOrderNumber(), application.getRefundApplicationNumber());
        }

        PaymentRefundRequest paymentRefundRequest = new PaymentRefundRequest();
        PaymentRefundResponse paymentRefundResponse = new PaymentRefundResponse();
        paymentRefundRequest.setPaymentOrder(paymentOrder);
        paymentRefundRequest.setRefundApplication(application);

        paymentRefundRouteService.route(paymentRefundRequest, paymentRefundResponse);
        application = paymentRefundResponse.getRefundApplication();

        refundResponse.setReturnCode(paymentRefundResponse.getReturnCode());
        refundResponse.setReturnMessage(paymentRefundResponse.getReturnMessage());
        refundResponse.setRefundAmount(application.getRefundAmount());
    }

    public List<Refund> queryRefundByPage(OrderQueryParamsVo paramsVo, PageInfo page) {
        String orderNumber = paramsVo.getOrderNumber();
        //String refundNumber = paramsVo.getRefundNumber();
        Date startDate = paramsVo.getStartDate();
        Date endDate = paramsVo.getEndDate();
        ServiceResult<List<RefundApplication>> refundsListResult = this.selectRefundList(orderNumber, null, paramsVo.getChannelCodeList(),
                startDate, endDate, page);
        List<RefundApplication> refundsList = refundsListResult.getData();
        //特殊需求处理：前端将orderNumber和refundNumber合并为一个字段传给后端，先去查询orderNumber，若查询不到值再去查询refundNumber
        if (refundsList == null || refundsList.size() == 0) {
            refundsListResult = this.selectRefundList(null, orderNumber, paramsVo.getChannelCodeList(),
                    startDate, endDate, page);
            refundsList = refundsListResult.getData();
        }
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
            if (RefundApplicationStatus.SUCCESS.equals(refundApp.getStatus())) {
                refund.setOrderRefundStatus(refundApp.getPaymentOrder().getStatus().getDescription());
            }
            if (refundApp.getCreateDate() != null) {
                String dateStr = DateFormatUtils.format(refundApp.getCreateDate(), "yyyy-MM-dd HH:mm:ss");
                refund.setCreateDate(dateStr);
            }
            result.add(refund);
        }
        return result;
    }


}
