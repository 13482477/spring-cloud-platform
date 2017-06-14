package com.siebre.payment.utils;

import com.siebre.payment.entity.enums.ReturnCode;

/**
 * Created by tianci.huang on 2017/6/14.
 */
public class MsgUtil {

    private ReturnCode result;

    private String msg;

    public MsgUtil() {
    }

    public MsgUtil(ReturnCode result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public ReturnCode getResult() {
        return result;
    }

    public void setResult(ReturnCode result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
