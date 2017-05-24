package com.siebre.payment.restful.paymentgateway.order;

import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.service.paymentorder.PaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
