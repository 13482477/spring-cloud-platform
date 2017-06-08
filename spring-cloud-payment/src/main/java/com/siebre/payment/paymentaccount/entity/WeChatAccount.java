package com.siebre.payment.paymentaccount.entity;

import java.io.Serializable;

/**
 * @author Huang Tianci
 *         微信账户
 */
public class WeChatAccount implements Serializable {

    private Long id;

    private String nickName;

    private String openid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
