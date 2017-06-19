package com.siebre.payment.entity.enums;

/**
 * Created by tianci.huang on 2017/6/19.
 */
public enum SubsequentAction {

    //调用微信js API
    INVOKE_WECHAT_JS_API("INVOKE_WECHAT_JS_API", "调用微信JS API"),

    //重定向到支付网关
    REDIRECT_TO_PAYMENT_GATEWAY("REDIRECT_TO_PAYMENT_GATEWAY", "重定向到支付网关"),

    //读取支付结果
    READ_PAY_RESULT("READ_PAY_RESULT", "读取支付结果"),
    ;

    SubsequentAction(String value, String description) {
        this.value = value;
        this.description = description;
    }

    private String value;

    private String description;

    public String getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

}
