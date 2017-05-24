package com.siebre.payment.restful.paymentgateway.query;

import com.siebre.payment.restful.basic.BaseController;
import com.siebre.payment.service.queryapplication.QueryApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentQueryController extends BaseController {

    @Autowired
    private QueryApplicationService queryApplicationService;

    /**
     * 到第三方渠道去查询订单
     * @param orderNumber
     * @return
     */
	@RequestMapping(value = "/paymentGateway/query", method = {RequestMethod.GET})
    @ResponseBody
    public PaymentOrderQueryResponse payQuery(@RequestParam("orderNumber") String orderNumber) throws Exception {
        PaymentOrderQueryRequest request = new PaymentOrderQueryRequest();
        request.setOrderNumber(orderNumber);
		PaymentOrderQueryResponse response = queryApplicationService.queryOrderStatusByOrderNumber(request).getData();
        return response;
    }

}
