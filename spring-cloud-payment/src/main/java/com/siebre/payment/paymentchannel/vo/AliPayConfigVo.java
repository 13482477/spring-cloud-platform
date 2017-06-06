package com.siebre.payment.paymentchannel.vo;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 支付宝配置信息vo模型
 */
public class AliPayConfigVo implements Serializable {

    /**
     * 合作伙伴id
     */
    private String pId;

    /**
     * 收款人银行账号
     */
    private String sellerAccount;

    /**
     * 即时到账支付
     */
    private AliPayWayConfigVo fastPayWay;

    /**
     * 手机网站支付
     */
    private AliPayWayConfigVo tradeWapPayWay;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public AliPayWayConfigVo getFastPayWay() {
        return fastPayWay;
    }

    public void setFastPayWay(AliPayWayConfigVo fastPayWay) {
        this.fastPayWay = fastPayWay;
    }

    public AliPayWayConfigVo getTradeWapPayWay() {
        return tradeWapPayWay;
    }

    public void setTradeWapPayWay(AliPayWayConfigVo tradeWapPayWay) {
        this.tradeWapPayWay = tradeWapPayWay;
    }
}
