package com.siebre.payment.restful.basic;

import com.siebre.basic.utils.HttpServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String getIpAddress(HttpServletRequest request) {
        return HttpServletRequestUtil.getIpAddress(request);
    }


    protected String getOpenId(HttpServletRequest request){
       return HttpServletRequestUtil.getOpenId(request);
    }

    protected Map<String, String> getParameterMap(HttpServletRequest request) {
        return HttpServletRequestUtil.getParameterMap(request);
    }


    /**
     * 校验金额是否合法
     * @param amount 金额字段
     * @param ifZero 是否可以为0
     * @return
     */
    protected boolean validateAmt(String amount,boolean ifZero){
        int indexNum = amount.indexOf(".");
        String lastStr = amount.substring(indexNum+1,amount.length());
        if(indexNum!=-1 && lastStr.length()>=3){
            return false;
        }
        try{
            BigDecimal amt=new BigDecimal(amount);
            if(ifZero){
                if(amt.doubleValue()<0){
                    return false;
                }
            }else{
                if(amt.doubleValue()<=0){
                    return false;
                }
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }



}
