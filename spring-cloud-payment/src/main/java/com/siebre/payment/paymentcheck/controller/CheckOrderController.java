package com.siebre.payment.paymentcheck.controller;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentOrderCheckStatus;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentOrderRefundStatus;
import com.siebre.payment.paymentcheck.vo.CheckOrderVo;
import com.siebre.payment.paymentcheck.vo.CheckOverviewResult;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author meilan 2017/6/6.
 * 对账管理接口
 */
@RestController
public class CheckOrderController {

    @Autowired
    private PaymentOrderService paymentOrderService;

    @ApiOperation(value = "对账总览", notes = "对账总览")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderNumber", dataType = "String", required = false, value = "订单号"),
            @ApiImplicitParam(paramType = "query", name = "channelCodeList", dataType = "Array", required = false, value = "渠道代码", allowableValues = "ALI_PAY,WECHAT_PAY,UNION_PAY,ALLIN_PAY,BAOFOO_PAY"),
            @ApiImplicitParam(paramType = "query", name = "payStatusList", dataType = "Array", required = false, value = "支付状态", allowableValues = "PAID,PART_REFUND,FULL_REFUND"),
            @ApiImplicitParam(paramType = "query", name = "checkStatusList", dataType = "Array", required = false, value = "对账状态", allowableValues = "NOT_CONFIRM,SUCCESS,FAIL,UNUSUAL"),
            @ApiImplicitParam(paramType = "query", name = "startDate", dataType = "Date", required = false, value = "对账开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endDate", dataType = "Date", required = false, value = "对账结束时间")
    })
    @RequestMapping(value = "/api/v1/checkOrders/Overview", method = GET)
    public ServiceResult<CheckOverviewResult> checkOverview(@Param("orderNumber") String orderNumber,@RequestParam(value = "channelCodeList", required = false) ArrayList<String> channelCodeList,
                                                            @RequestParam(value = "payStatusList", required = false) ArrayList<PaymentOrderPayStatus> payStatusList,
                                                            @RequestParam(value = "checkStatusList", required = false) ArrayList<PaymentOrderCheckStatus> checkStatusList,
                                                            @Param("startDate")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                            @Param("endDate")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {

        CheckOverviewResult checkOverviewResult = paymentOrderService.getOrdersOverview(orderNumber, channelCodeList, payStatusList, checkStatusList, startDate,endDate);
        return ServiceResult.<CheckOverviewResult>builder().success(Boolean.TRUE).data(checkOverviewResult).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    @ApiOperation(value = "对账明细列表", notes = "对账明细列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderNumber", dataType = "String", required = false, value = "订单号"),
            @ApiImplicitParam(paramType = "query", name = "channelCodeList", dataType = "Array", required = false, value = "渠道代码", allowableValues = "ALI_PAY,WECHAT_PAY,UNION_PAY,ALLIN_PAY,BAOFOO_PAY"),
            @ApiImplicitParam(paramType = "query", name = "payStatusList", dataType = "Array", required = false, value = "支付状态", allowableValues = "PAID,PART_REFUND,FULL_REFUND"),
            @ApiImplicitParam(paramType = "query", name = "checkStatusList", dataType = "Array", required = false, value = "对账状态", allowableValues = "NOT_CONFIRM,SUCCESS,FAIL,UNUSUAL"),
            @ApiImplicitParam(paramType = "query", name = "startDate", dataType = "Date", required = false, value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endDate", dataType = "Date", required = false, value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "showCount", dataType = "Integer", required = false, value = "每页显示数据行数"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", dataType = "Integer", required = false, value = "总页数")
    })
    @RequestMapping(value = "/api/v1/checkOrders", method = GET)
    public ServiceResult<List<CheckOrderVo>> selectCheckOrderByPage(@Param("orderNumber") String orderNumber, @RequestParam(value = "channelCodeList", required = false) ArrayList<String> channelCodeList,
                                                                    @RequestParam(value = "payStatusList", required = false) ArrayList<PaymentOrderPayStatus> payStatusList,
                                                                    @RequestParam(value = "checkStatusList", required = false) ArrayList<PaymentOrderCheckStatus> checkStatusList,
                                                                    @Param("startDate")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                                    @Param("endDate")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate,
                                                                    @Param("showCount") Integer showCount, @Param("currentPage") Integer currentPage) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setShowCount(showCount == null ? 10 : showCount);
        pageInfo.setCurrentPage(currentPage == null ? 1 : currentPage);
        return paymentOrderService.selectCheckOrderByPage(orderNumber, channelCodeList, payStatusList, checkStatusList, startDate,endDate,pageInfo);
    }

    @ApiOperation(value = "对账明细列表详情", notes = "对账明细列表详情")
    @RequestMapping(value = "/api/v1/checkOrders/{orderNumber}", method = GET)
    public ServiceResult<CheckOrderVo> checkOrderDetail(@PathVariable String orderNumber) {
        CheckOrderVo orderVo = paymentOrderService.queryPaymentOrderForCheckDetail(orderNumber);
        return ServiceResult.<CheckOrderVo>builder().success(Boolean.TRUE).data(orderVo).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

}
