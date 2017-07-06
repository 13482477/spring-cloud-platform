package com.siebre.payment.paymentgateway.controller;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.basic.service.ServiceResult;
import com.siebre.basic.utils.HttpServletRequestUtil;
import com.siebre.payment.entity.enums.PaymentOrderLockStatus;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymentgateway.vo.*;
import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.config.HandlerBeanNameConfig;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.paymenthandler.wechatpay.WeChatPublicAuthService;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymentroute.service.PaymentRefundRouteService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.refundapplication.service.RefundApplicationService;
import com.siebre.payment.service.queryapplication.QueryApplicationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class PaymentGatewayController {

    @Autowired
    private PaymentWayService paymentWayService;

    @Autowired
    private WeChatPublicAuthService weChatPublicAuthService;

    @Autowired
    private QueryApplicationService queryApplicationService;

    @Autowired
    private RefundApplicationService refundApplicationService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    /**
     * 到第三方渠道去查询订单
     *
     * @param orderNumber
     * @return
     */
    @ApiOperation(value = "查询订单支付状态（去第三方渠道）", notes = "到第三方渠道去查询订单")
    @RequestMapping(value = "/openApi/v1/paymentGateway/query/{orderNumber}", method = GET)
    public PaymentQueryResponse payQuery(@PathVariable String orderNumber) throws Exception {
        PaymentQueryResponse response = queryApplicationService.queryOrderStatusByOrderNumber(orderNumber);
        return response;
    }

    @ApiOperation(value = "统一支付接口(V2.0)", notes = "统一支付接口(V2.0)")
    @RequestMapping(value = "/openApi/v2/paymentGateway/unifiedPay", method = POST)
    public UnifiedPayResponse unipayV2(@RequestBody UnifiedPayRequest unifiedPayRequest, HttpServletRequest request) {
        UnifiedPayResponse response = new UnifiedPayResponse();
        PaymentOrderResponse orderResponse = paymentOrderService.creatPaymentOrder(unifiedPayRequest);
        if (ReturnCode.FAIL.getDescription().equals(orderResponse.getReturnCode())) {
            response.setReturnCode(orderResponse.getReturnCode());
            response.setReturnMessage(orderResponse.getReturnMessage());
            return response;
        }
        String handlerBeanName = HandlerBeanNameConfig.PAY_MAPPING.get(unifiedPayRequest.getPaymentOrder().getPaymentWayCode());
        PaymentRequest paymentRequest = assemblePaymentRequest(request, orderResponse);
        PaymentResponse paymentResponse = new PaymentResponse();
        AbstractPaymentComponent paymentComponent = (AbstractPaymentComponent) SpringContextUtil.getBean(handlerBeanName);
        paymentComponent.handle(paymentRequest, paymentResponse);
        response = assembleUnifiedPayResponse(unifiedPayRequest, paymentResponse);
        return response;
    }

    private UnifiedPayResponse assembleUnifiedPayResponse(UnifiedPayRequest unifiedPayRequest, PaymentResponse paymentResponse) {
        UnifiedPayResponse response = new UnifiedPayResponse();
        response.setMessageId(unifiedPayRequest.getMessageId());
        response.setRedirectUrl(paymentResponse.getPayUrl());
        response.setReturnCode(paymentResponse.getReturnCode());
        response.setReturnMessage(paymentResponse.getReturnMessage());
        response.setSubsequentAction(paymentResponse.getSubsequentAction());
        if (paymentResponse.getWechatJsApiParams() != null) {
            response.setWeChatJsApiParams(paymentResponse.getWechatJsApiParams());
        }
        response.setPaymentOrder(assembleUnifiedPayResOrder(paymentResponse.getPaymentOrder()));
        return response;
    }

    private PaymentRequest assemblePaymentRequest(HttpServletRequest request, PaymentOrderResponse orderResponse) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentWayCode(orderResponse.getPaymentOrder().getPaymentWayCode());
        paymentRequest.setOrderNumber(orderResponse.getPaymentOrder().getOrderNumber());
        paymentRequest.setIp(HttpServletRequestUtil.getIpAddress(request));
        if(orderResponse.getPaymentOrder().getPaymentAccount() != null) {
            paymentRequest.setOpenid(orderResponse.getPaymentOrder().getPaymentAccount().getOpenid());
        }
        paymentRequest.setPaymentOrder(orderResponse.getPaymentOrder());
        return paymentRequest;
    }

    private UnifiedPayResOrder assembleUnifiedPayResOrder(PaymentOrder paymentOrder) {
        UnifiedPayResOrder resOrder = new UnifiedPayResOrder();
        resOrder.setPaymentWayCode(paymentOrder.getPaymentWayCode());
        resOrder.setOrderNumber(paymentOrder.getOrderNumber());
        resOrder.setStatus(paymentOrder.getStatus().getDescription());
        resOrder.setCreatedOn(paymentOrder.getCreateTime());
        return resOrder;
    }

    /**
     * 统一退款（v2.0）
     *
     * @param refundRequest
     * @return
     */
    @ApiOperation(value = "统一单笔退款接口(V2.0)", notes = "统一单笔退款接口(V2.0)")
    @RequestMapping(value = "/openApi/v2/paymentGateWay/refund", method = POST)
    public RefundResponse applicationRefund2(@RequestBody RefundRequest refundRequest) {
        RefundResponse refundResponse = new RefundResponse();
        refundApplicationService.doRefund(refundRequest, refundResponse);
        return refundResponse;
    }

    /**
     * 获取请求微信授权的url pageUrl 微信需要重定向的目标页面，会将code返回回来
     */
    @ApiOperation(value = "获取微信授权", notes = "获取请求微信授权的redirectUrl:微信需要重定向的目标页面，会将code返回回来")
    @RequestMapping(value = "/paymentGateway/unifiedPay/weChatAuthorize", method = GET)
    public Object weChatTest(HttpServletRequest request) {
        String pageUrl = request.getParameter("pageUrl");
        Map<String, String> result = new HashMap<>();
        PaymentWay paymentWay = this.paymentWayService.getPaymentWayByCode("WECHAT_PUBLIC_PAY").getData();
        String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + paymentWay.getAppId() + "&redirect_uri=" + pageUrl
                + "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
        result.put("redirectUrl", redirectUrl);
        return result;
    }

    /**
     * 获取微信openid
     * @param request
     * @return
     */
    @ApiOperation(value = "获取微信openid", notes = "获取微信openid")
    @RequestMapping(value = "/paymentGateway/unifiedPay/getWeChatOpenid", method = GET)
    public Object getWeChatOpenid(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        String openid = weChatPublicAuthService.getOpenID(request.getParameter("code"));
        result.put("openid", openid);
        return result;
    }

}
