package com.siebre.payment.vo.unionpayment;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * Title: Class PayQueryParamDTO
 * Description:
 *	支付查询，用于发起查询的必要参数
 *
 * @author chunling.yan
 * @email    
*  @version 1.0 2017年3月14日 
 */
public class PayQueryParamVo implements Serializable{

	private static final long serialVersionUID = 4231942882144054731L;
	

	/**
	 * 支付交易号
	 */
    private String tradeNo;
    /*
     * 三方返回支付交易号
     */
    private String returnTradeNo;
    private String payWayCode;
    private HashMap<String,String> map;
    /**
     * 渠道流水号
     */
    private	String responseOrder;
	/**
	 * 支付时间
	 */
	private String payTime;



	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getReturnTradeNo() {
		return returnTradeNo;
	}

	public void setReturnTradeNo(String returnTradeNo) {
		this.returnTradeNo = returnTradeNo;
	}

	public String getResponseOrder() {
		return responseOrder;
	}

	public void setResponseOrder(String responseOrder) {
		this.responseOrder = responseOrder;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}



	@Override
	public String toString() {
		return "PayQueryParamDTO [tradeNo=" + tradeNo + ", returnTradeNo=" + returnTradeNo + ", payWayCode="
				+ payWayCode + ", map=" + map + ", responseOrder=" + responseOrder + ", payTime=" + payTime + "]";
	}

	public String getPayWayCode() {
		return payWayCode;
	}

	public void setPayWayCode(String payWayCode) {
		this.payWayCode = payWayCode;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}





    

    
    
}
