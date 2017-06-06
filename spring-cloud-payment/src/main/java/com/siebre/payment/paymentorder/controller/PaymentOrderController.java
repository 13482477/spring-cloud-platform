package com.siebre.payment.paymentorder.controller;

import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymentgateway.vo.PaymentOrderRequest;
import com.siebre.payment.paymentgateway.vo.PaymentOrderResponse;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Huang Tianci
 * 订单生成接口
 */

@RestController
public class PaymentOrderController {

    @Autowired
    private PaymentOrderService paymentOrderService;

    @RequestMapping(value = "/api/v1/paymentOrder", method = POST)
    public PaymentOrderResponse createOrder(@RequestBody PaymentOrderRequest orderRequest, HttpServletRequest request){
        PaymentOrderResponse response = paymentOrderService.createPaymentOrderAndItems(orderRequest,request);
        return response;
    }

    @RequestMapping(value = "/api/v1/paymentOrder/{orderNumber}", method = GET)
    public WebResult<PaymentOrder> queryByOrderNumber(@PathVariable String orderNumber) {
        PaymentOrder order = paymentOrderService.queryPaymentOrder(orderNumber);
        return WebResult.<PaymentOrder>builder().returnCode("200").returnMessage("调用成功").data(order).build();
    }
    
    @RequestMapping(value = "/api/v1/paymentOrders", method = GET)
    public WebResult<List<PaymentOrder>> queryByPage() {
    	
    	//TODO
    	
    	return WebResult.<List<PaymentOrder>>builder().returnCode("200").returnMessage("调用成功").build();
    }
    
    
}
