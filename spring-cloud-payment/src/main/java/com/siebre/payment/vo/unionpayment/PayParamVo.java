package com.siebre.payment.vo.unionpayment;

import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Title: Class PayItemParamDTO
 * Description:
 *	支付明细对象，用于发起支付的必要参数
 *
 * @author chunling.yan
 * @email  chunling.yan@siebresystems.com
*  @version 1.0 2017年3月14日 
 */
public class PayParamVo implements Serializable{
	private static final long serialVersionUID = 4231942882144054731L;
	
	private String payChannel;
	private String merPayOrder;
	private String payAmt;
	private List<PaymentOrderItem> paymentOrderItems;//投保单s
	private String payWayCode;
	private String transactionNumber;

	private String orderSubject;
	private String ip;

	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	public String getMerPayOrder() {
		return merPayOrder;
	}
	public void setMerPayOrder(String merPayOrder) {
		this.merPayOrder = merPayOrder;
	}
	public String getPayAmt() {
		return payAmt;
	}
	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}
	public List<PaymentOrderItem> getPaymentOrderItems() {
		return paymentOrderItems;
	}
	public void setPaymentOrderItems(List<PaymentOrderItem> paymentOrderItems) {
		this.paymentOrderItems = paymentOrderItems;
	}
	public String getPayWayCode() {
		return payWayCode;
	}
	public void setPayWayCode(String payWayCode) {
		this.payWayCode = payWayCode;
	}
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

//	private String payerAcct;

	public String getOrderSubject() {
		return orderSubject;
	}

	public void setOrderSubject(String orderSubject) {
		this.orderSubject = orderSubject;
	}


	//	private String payerAcct;
//	private String sellerAcct;
//	private String orderSource;
//	private String equipNo;
//	private String equipIp;
//	private String remark;
//	private String merPayOrder;
//	private String payTerminalType; // 支付终端app，pc
//	private String tradeType;
//	private String payChannel;
//	private String merNo;// 商户号
//	private String merName;
//	private String payWayCode; // 支付方式
//	private String payAmt;
//	private String payFee;
//	private String currencyType = "CNY";
//	private HashMap<String,String> paramMap;
//	public String getPayerAcct() {
//		return payerAcct;
//	}
//	public void setPayerAcct(String payerAcct) {
//		this.payerAcct = payerAcct;
//	}
//	public String getSellerAcct() {
//		return sellerAcct;
//	}
//	public void setSellerAcct(String sellerAcct) {
//		this.sellerAcct = sellerAcct;
//	}
//	public String getOrderSource() {
//		return orderSource;
//	}
//	public void setOrderSource(String orderSource) {
//		this.orderSource = orderSource;
//	}
//	public String getEquipNo() {
//		return equipNo;
//	}
//	public void setEquipNo(String equipNo) {
//		this.equipNo = equipNo;
//	}
//	public String getEquipIp() {
//		return equipIp;
//	}
//	public void setEquipIp(String equipIp) {
//		this.equipIp = equipIp;
//	}
//	public String getRemark() {
//		return remark;
//	}
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
//	public String getMerPayOrder() {
//		return merPayOrder;
//	}
//	public void setMerPayOrder(String merPayOrder) {
//		this.merPayOrder = merPayOrder;
//	}
//	public String getPayTerminalType() {
//		return payTerminalType;
//	}
//	public void setPayTerminalType(String payTerminalType) {
//		this.payTerminalType = payTerminalType;
//	}
//	public String getTradeType() {
//		return tradeType;
//	}
//	public void setTradeType(String tradeType) {
//		this.tradeType = tradeType;
//	}
//	public String getPayChannel() {
//		return payChannel;
//	}
//	public void setPayChannel(String payChannel) {
//		this.payChannel = payChannel;
//	}
//	public String getMerNo() {
//		return merNo;
//	}
//	public void setMerNo(String merNo) {
//		this.merNo = merNo;
//	}
//	public String getMerName() {
//		return merName;
//	}
//	public void setMerName(String merName) {
//		this.merName = merName;
//	}
//	public String getPayWayCode() {
//		return payWayCode;
//	}
//	public void setPayWayCode(String payWayCode) {
//		this.payWayCode = payWayCode;
//	}
//	public String getPayAmt() {
//		return payAmt;
//	}
//	public void setPayAmt(String payAmt) {
//		this.payAmt = payAmt;
//	}
//	public String getPayFee() {
//		return payFee;
//	}
//	public void setPayFee(String payFee) {
//		this.payFee = payFee;
//	}
//	public String getCurrencyType() {
//		return currencyType;
//	}
//	public void setCurrencyType(String currencyType) {
//		this.currencyType = currencyType;
//	}
//	public HashMap<String, String> getParamMap() {
//		return paramMap;
//	}
//	public void setParamMap(HashMap<String, String> paramMap) {
//		this.paramMap = paramMap;
//	}

    
    
}
