package com.siebre.payment.controller.paymentgateway.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.payment.service.queryapplication.QueryApplicationService;

@RestController
public class PaymentQueryController {

    @Autowired
    private QueryApplicationService queryApplicationService;

    /**
     * 到第三方渠道去查询订单
     * @param orderNumber
     * @return
     */
	@RequestMapping(value = "/api/v1/paymentGateway/query", method = {RequestMethod.GET})
    public PaymentOrderQueryResponse payQuery(@RequestParam("orderNumber") String orderNumber) throws Exception {
        PaymentOrderQueryRequest request = new PaymentOrderQueryRequest();
        request.setOrderNumber(orderNumber);
		PaymentOrderQueryResponse response = queryApplicationService.queryOrderStatusByOrderNumber(request).getData();
        return response;
    }

}
