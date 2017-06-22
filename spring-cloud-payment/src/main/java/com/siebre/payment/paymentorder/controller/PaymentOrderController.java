package com.siebre.payment.paymentorder.controller;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymentorder.vo.OrderQueryParamsVo;
import com.siebre.payment.paymentorder.vo.TradeOrder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
        return WebResult.<List<TradeOrder>>builder().returnCode("200").data(tradeOrders).pageInfo(page).returnMessage("调用成功").build();
    }

    @ApiOperation(value="订单详情", notes = "订单详情")
    @RequestMapping(value = "/api/v1/paymentOrders/{orderNumber}", method = GET)
    public WebResult<TradeOrder> loadOrderDetail(@PathVariable String orderNumber) {
        TradeOrder tradeOrderDetail = paymentOrderService.loadOrderDetail(orderNumber);
        return WebResult.<TradeOrder>builder().returnCode("200").data(tradeOrderDetail).returnMessage("调用成功").build();
    }

}
