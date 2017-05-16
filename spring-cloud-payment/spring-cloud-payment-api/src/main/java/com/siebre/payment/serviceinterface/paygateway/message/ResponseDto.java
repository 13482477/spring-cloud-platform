/**
 * Created on 2016年11月4日
 * filename: ResponseDto.java
 * Description: 
 *
 */
package com.siebre.payment.serviceinterface.paygateway.message;

import java.io.Serializable;

/**
 * Title: Class ResponseDto
 * Description:
 *
 *	渠道返回报文信息
 * @author chunling.yan
 * @email    
*  @version 1.0 2017年3月14日 
 */
public class ResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -536505758568414532L;
	
	
	/**
	 * 第三方返回的业务code
	 */
	protected String responseCode;
	
	/**
	 * 第三方返回的业务code对应的内容
	 */
	protected String responseMessage;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "ResponseDto [responseCode=" + responseCode
				+ ", responseMessage=" + responseMessage + "]";
	}
	
	

}
