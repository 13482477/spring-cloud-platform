package com.siebre.payment.restful.paymentgateway.refund;

import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.entity.refundapplication.RefundApplication;
import com.siebre.payment.restful.basic.BaseController;
import com.siebre.payment.service.paymentroute.PaymentRefundRouteService;
import com.siebre.payment.service.paymenttransaction.PaymentTransactionService;
import com.siebre.payment.service.refundapplication.RefundApplicationService;
import com.siebre.payment.serviceinterface.paymenthandler.paymentrefund.PaymentRefundRequest;
import com.siebre.payment.serviceinterface.paymenthandler.paymentrefund.PaymentRefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 对外开放退款申请接口
 */
@Controller
public class RefundApplicationController extends BaseController {

    @Autowired
    private RefundApplicationService refundApplicationService;

    @Autowired
    private PaymentRefundRouteService paymentRefundRouteService;

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @ResponseBody
    @RequestMapping(value = "/paymentGateWay/refund", method = {RequestMethod.POST, RequestMethod.GET})
    public RefundResponse applicationRefund(@RequestBody RefundRequest refundRequest, HttpServletRequest servletRequest) {
        RefundApplication application = new RefundApplication();
        application.setStatus(RefundApplicationStatus.APPLICATION);
        application.setRequest(refundRequest.getRequest());
        application.setOrderNumber(refundRequest.getOrderNumber());
        application.setRefundAmount(refundRequest.getRefundAmount());

        //创建RefundApplication
        refundApplicationService.createRefundApplication(application);

        if(RefundApplicationStatus.APPLICATION.equals(application.getStatus())){
            PaymentRefundRequest paymentRefundRequest = new PaymentRefundRequest();

            paymentRefundRequest.setRefundApplication(application);
            PaymentRefundResponse refundResponse = paymentRefundRouteService.route(paymentRefundRequest);

            application = refundResponse.getRefundApplication();
        }

        RefundResponse refundResponse = new RefundResponse();

        refundResponse.setRefundStatus(application.getStatus().toString());

        refundResponse.setResponse(application.getResponse());

        refundResponse.setApplicationNumber(application.getApplicationNumber());
        return refundResponse;
    }
}
