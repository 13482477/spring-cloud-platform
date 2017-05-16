/**
 * Created on 2016年10月9日
 * filename: PayItemResultDTO.java
 * Description: 
 *
 */
package com.siebre.payment.vo.unionpayment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.siebre.payment.serviceinterface.paygateway.message.TradeBaseResultDto;




/**
 * Title: Class PayItemResultDTO
 * Description:
 *	支付结果对象
 *
 * @author chunling.yan
 * @email    
*  @version 1.0 2017年3月14日 
 */
public class PayQueryResponseVo  implements Serializable {
    
    /**
     * 最终响应编码,第三方响应的状态需要转换成支付平台标准
     * 处理中 , 成功 , 失败
     */
    private String respStatus;

    /**
     * 最终响应结果参数集合
     */
    private String respContent;
    
    private HashMap<String,String> respMap;
    /*
     * 参数签名
     */
    private String sign;



	public String getRespContent() {
		return respContent;
	}

	public void setRespContent(String respContent) {
		this.respContent = respContent;
	}



	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public HashMap getRespMap() {
		return respMap;
	}

	public void setRespMap(Map<String, String> respMap) {
		this.respMap = (HashMap<String, String>) respMap;
	}

	public String getRespStatus() {
		return respStatus;
	}

	public void setRespStatus(String respStatus) {
		this.respStatus = respStatus;
	}
    
   
    
    
}
