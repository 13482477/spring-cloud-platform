package com.siebre.payment.controller.paymentgateway.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.service.paymentorder.PaymentOrderService;

/**
 * @author Huang Tianci
 * 订单生成接口
 */

@RestController
public class PaymentOrderController {

    @Autowired
    private PaymentOrderService paymentOrderService;

    @RequestMapping(value = "/paymentOrder/create", method = {RequestMethod.GET,RequestMethod.POST}, consumes = "application/json")
    public PaymentOrderResponse createOrder(@RequestBody PaymentOrderRequest orderRequest, HttpServletRequest request){
        PaymentOrderResponse response = paymentOrderService.createPaymentOrderAndItems(orderRequest,request);
        return response;
    }

    @RequestMapping(value = "/paymentOrder/query", method = {RequestMethod.GET})
    @ResponseBody
    public PaymentOrderResponse query(HttpServletRequest request) {
        String orderNumber = request.getParameter("orderNumber");
        PaymentOrder order = paymentOrderService.queryPaymentOrder(orderNumber);
        PaymentOrderResponse response = PaymentOrderResponse.SUCCESS("ok", order, order.getPaymentOrderItems());
        return response;
    }
}
