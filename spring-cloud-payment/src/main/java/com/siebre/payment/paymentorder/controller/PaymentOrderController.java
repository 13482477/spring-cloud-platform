package com.siebre.payment.paymentorder.controller;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Huang Tianci
 * 订单生成接口
 */

@RestController
public class PaymentOrderController {

    @Autowired
    private PaymentOrderService paymentOrderService;

    @RequestMapping(value = "/api/v1/paymentOrder", method = {RequestMethod.POST})
    public PaymentOrder create(@RequestParam String paymentWayCode, @RequestBody PaymentOrder paymentOrder){
        //paymentOrderService.createPaymentOrderAndItems(paymentWayCode, paymentOrder);
        return null;
    }

    @RequestMapping(value = "/api/v1/paymentOrder/{orderNumber}", method = {RequestMethod.GET})
    public WebResult<PaymentOrder> queryByOrderNumber(@PathVariable String orderNumber) {
        PaymentOrder order = paymentOrderService.queryPaymentOrder(orderNumber);
        return WebResult.<PaymentOrder>builder().returnCode("200").returnMessage("调用成功").data(order).build();
    }
    
    @RequestMapping(value = "/api/v1/paymentOrders", method = {RequestMethod.GET})
    public WebResult<List<PaymentOrder>> queryByPage(
    		@RequestParam String orderNumber, 
    		@RequestParam PaymentOrderPayStatus orderPayStatus, 
    		@RequestParam String channelName,
    		@RequestParam Date startDate, 
    		@RequestParam Date endDate,
            @RequestParam Integer page, 
            @RequestParam Integer limit, 
            @RequestParam String sortField, 
            @RequestParam String order
            ) {
    	
    	PageInfo pageInfo = new PageInfo(page, limit, sortField, order);
    	
    	//List<PaymentOrder> orders = this.paymentOrderService.getOrderListForPage(orderNumber, orderPayStatus, channelName, startDate, endDate, pageInfo);
    	
    	return WebResult.<List<PaymentOrder>>builder().returnCode("200").returnMessage("调用成功").pageInfo(pageInfo).build();
    }
    
    
}
