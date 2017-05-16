package com.siebre.payment.serviceinterface.paymenthandler.wechatpay;

/**
 * @author Huang Tianci
 */
public interface IWeChatPublicAuthService {

    String getOpenID(String code);

}
