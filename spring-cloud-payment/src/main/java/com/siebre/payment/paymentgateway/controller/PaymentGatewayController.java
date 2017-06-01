package com.siebre.payment.paymentgateway.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.paymentgateway.vo.PaymentOrderQueryRequest;
import com.siebre.payment.paymentgateway.vo.PaymentOrderQueryResponse;
import com.siebre.payment.paymentgateway.vo.RefundRequest;
import com.siebre.payment.paymentgateway.vo.RefundResponse;
import com.siebre.payment.paymentgateway.vo.UnifiedPayRequest;
import com.siebre.payment.paymentgateway.vo.UnifiedPayResponse;
import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymenthandler.wechatpay.WeChatPublicAuthService;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentroute.service.PaymentRefundRouteService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.refundapplication.service.RefundApplicationService;
import com.siebre.payment.service.queryapplication.QueryApplicationService;

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
	private PaymentRefundRouteService paymentRefundRouteService;

	/**
	 * 到第三方渠道去查询订单
	 * 
	 * @param orderNumber
	 * @return
	 */
	@RequestMapping(value = "/openApi/v1/paymentGateway/query", method = { RequestMethod.GET })
	public PaymentOrderQueryResponse payQuery(@RequestParam("orderNumber") String orderNumber) throws Exception {
		PaymentOrderQueryRequest request = new PaymentOrderQueryRequest();
		request.setOrderNumber(orderNumber);
		PaymentOrderQueryResponse response = queryApplicationService.queryOrderStatusByOrderNumber(request).getData();
		return response;
	}

	/**
	 * 统一支付接口
	 * 
	 * @param unipayRequest
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/openApi/v1/paymentGateway/unifiedPay", method = { RequestMethod.POST })
	public UnifiedPayResponse unipay(@RequestBody UnifiedPayRequest unipayRequest, HttpServletRequest request) {
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setPaymentWayCode(unipayRequest.getPayWayCode());
		paymentRequest.setOrderNumber(unipayRequest.getOrderNumber());
		paymentRequest.setPaymentOrderItems(unipayRequest.getPaymentOrderItems());
		paymentRequest.setIp(null);
		paymentRequest.setOpenid(unipayRequest.getOpenid());

		PaymentInterface paymentInterface = this.paymentWayService.getPaymentInterface(paymentRequest.getPaymentWayCode(), PaymentInterfaceType.PAY);

		AbstractPaymentComponent paymentComponent = (AbstractPaymentComponent) SpringContextUtil.getBean(paymentInterface.getHandlerBeanName());

		PaymentResponse paymentResponse = paymentComponent.handle(paymentRequest);

		UnifiedPayResponse response = new UnifiedPayResponse();
		response.setPaymentUrl(paymentResponse.getPayUrl());
		response.setBody(paymentResponse.getBody());
		return response;
	}

	/**
	 * 统一退款接口
	 * 
	 * @param refundRequest
	 * @param servletRequest
	 * @return
	 */
	@RequestMapping(value = "/openApi/v1/paymentGateWay/refund", method = { RequestMethod.POST })
	public RefundResponse applicationRefund(@RequestBody RefundRequest refundRequest, HttpServletRequest servletRequest) {
		RefundApplication application = new RefundApplication();
		application.setStatus(RefundApplicationStatus.APPLICATION);
		application.setRequest(refundRequest.getRequest());
		application.setOrderNumber(refundRequest.getOrderNumber());
		application.setRefundAmount(refundRequest.getRefundAmount());

		// 创建RefundApplication
		refundApplicationService.createRefundApplication(application);

		if (RefundApplicationStatus.APPLICATION.equals(application.getStatus())) {
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

	/**
	 * 获取请求微信授权的url pageUrl 微信需要重定向的目标页面，会将code返回回来
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/paymentGateway/unifiedPay/wechatauthorize", method = { RequestMethod.GET })
	public Object weChatTest(HttpServletRequest request) {
		String pageUrl = request.getParameter("pageUrl");
		Map<String, String> result = new HashMap<>();
		PaymentWay paymentWay = this.paymentWayService.getPaymentWayByCode("WECHAT_PUBLIC_PAY");
		String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + paymentWay.getAppId() + "&redirect_uri=" + pageUrl
				+ "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
		result.put("redirectUrl", redirectUrl);
		return result;
	}

	/**
	 * 获取微信openid
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/paymentGateway/unifiedPay/getWeChatOpenid", method = { RequestMethod.GET })
	public Object getWeChatOpenid(HttpServletRequest request) {
		Map<String, String> result = new HashMap<>();
		String openid = weChatPublicAuthService.getOpenID(request.getParameter("code"));
		result.put("openid", openid);
		return result;
	}

}
