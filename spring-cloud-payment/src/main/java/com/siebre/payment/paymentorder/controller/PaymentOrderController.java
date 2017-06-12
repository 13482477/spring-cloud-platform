package com.siebre.payment.paymentorder.controller;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymentgateway.vo.PaymentOrderRequest;
import com.siebre.payment.paymentgateway.vo.PaymentOrderResponse;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymentorder.vo.OrderQueryParamsVo;
import com.siebre.payment.paymentorder.vo.TradeOrder;
import com.siebre.payment.paymentorder.vo.TradeOrderDetail;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Huang Tianci
 *         订单生成接口
 */

@RestController
public class PaymentOrderController {

    @Autowired
    private PaymentOrderService paymentOrderService;

    @ApiOperation(value="订单列表", notes = "订单列表")
    @RequestMapping(value = "/api/v1/paymentOrders", method = GET)
    public WebResult<List<TradeOrder>> queryOrderByPage(OrderQueryParamsVo paramsVo) {
        PageInfo page = new PageInfo();
        page.setCurrentPage(paramsVo.getCurrentPage());
        page.setShowCount(paramsVo.getShowCount());
        List<TradeOrder> tradeOrders = paymentOrderService.queryOrderByPage(paramsVo, page);
        return WebResult.<List<TradeOrder>>builder().pageInfo(page).data(tradeOrders).build();
    }

    @ApiOperation(value="订单详情", notes = "订单详情")
    @RequestMapping(value = "/api/v1/paymentOrders/{orderNumber}", method = GET)
    public WebResult<TradeOrderDetail> loadOrderDetail(@PathVariable String orderNumber) {
        TradeOrderDetail tradeOrderDetail = paymentOrderService.loadOrderDetail(orderNumber);
        return WebResult.<TradeOrderDetail>builder().returnCode("200").data(tradeOrderDetail).returnMessage("调用成功").build();
    }

}
