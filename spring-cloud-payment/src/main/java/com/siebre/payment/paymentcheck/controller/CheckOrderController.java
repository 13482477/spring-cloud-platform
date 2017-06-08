package com.siebre.payment.paymentcheck.controller;

import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.paymentcheck.vo.CheckOverviewResult;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author meilan 2017/6/6.
 *         对账管理接口
 */
@RestController
public class CheckOrderController {

    @Autowired
    private PaymentOrderService paymentOrderService;

    @ApiOperation(value = "对账总览", notes = "对账总览")
    @RequestMapping(value = "/api/v1/checkOrders", method = GET)
    public ServiceResult<CheckOverviewResult> checkOverview(@Param("channelId") Long channelId, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date checkStartDate,
                                                            @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date checkEndDate) {

        CheckOverviewResult checkOverviewResult = paymentOrderService.getOrdersByChannelAndDate(channelId, checkStartDate, checkEndDate);
        return ServiceResult.<CheckOverviewResult>builder().success(Boolean.TRUE).data(checkOverviewResult).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

}
