package com.siebre.payment.serviceinterface.paygateway.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * 
 * Title: Class TradeResultDto Description: 支付返回信息
 *
 * @author chunling.yan
 * @email   
*  @version 1.0 2017年3月14日 
 */
public abstract class TradeResultDto implements Serializable{



	private static final long serialVersionUID = -3618771525421504750L;

	/**
	 * 最终响应编码,第三方响应的状态需要转换成支付平台标准 处理中 , 成功 , 失败
	 */
	protected String tradeStatus;

	/**
	 * 附件信息，第三方返回的报文，需要响应到调用方的信息
	 */
	protected Object attachment;
	

	/**
	 * 请求报文信息列表
	 */
	protected List<Message> requestList = Collections.emptyList();

	/**
	 * 响应报文信息列表
	 */
	protected List<Message> responseList = Collections.emptyList();

	/**
	 * 添加请求报文消息
	 *
	 * @param request
	 */
	public void addRequestMessage(Message request) {
		if (requestList.isEmpty())
			requestList = new ArrayList<>(2);
		requestList.add(request);
	}

	/**
	 * 添加响应报文信息
	 *
	 * @param response
	 */
	public void addResponseMessage(Message response) {
		if (responseList.isEmpty())
			responseList = new ArrayList<>(2);
		this.responseList.add(response);
	}

	/**
	 * 
	 * 本次操作是否有结果
	 * Created on 2016年10月11日
	 * @author chunling.yan
*  @version 1.0 2017年3月14日 
	 * @return
	 */
	public boolean hasResult() {
		return tradeStatus != null & attachment != null;
	}

	/**
	 * 
	 * 本次操作是否有明细
	 * Created on 2016年10月11日
	 * @author chunling.yan
*  @version 1.0 2017年3月14日 
	 * @return
	 */
	public abstract boolean hasItem();


	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

	public List<Message> getRequestList() {
		return requestList;
	}

	public List<Message> getResponseList() {
		return responseList;
	}
	
	


	@Override
	public String toString() {
		return "TradeResultDto [status=" + tradeStatus + ", attachment="
				+ attachment + ", requestList=" + requestList
				+ ", responseList=" + responseList + "]";
	}



	
	

}
