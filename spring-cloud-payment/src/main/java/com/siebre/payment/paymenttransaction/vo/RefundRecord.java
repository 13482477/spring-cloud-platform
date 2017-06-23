package com.siebre.payment.paymenttransaction.vo;

import java.io.Serializable;

/**
 * 退款详情记录
 */
public class RefundRecord implements Serializable {

    private String flag;

    private String time;

    private String desc;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
