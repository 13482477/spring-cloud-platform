package com.siebre.payment.paymenthandler.baofoo.pay.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 宝付 统一返回信息
 */
public class BaofooResponse  {

    //返回是否成功
    private boolean success;

    //-------宝付应答业务信息
    //应答码0000成功
    private String responseCode;

    //应答信息
    private String responseMessage;

    //宝付业务流水号
    private String businessNumber;

    //宝付交易号
    private String externalNumber;

    //订单状态
    private String orderState;

    //成功支付金额
    private String successAmount;

    //预绑卡代码
    private String preBindCode;

    //绑卡ID
    private String bindId;

    //卡号
    private String cardNo;

    //银行编码
    private String bankCode;

    //银行名称
    private String bankName;

    //卡类型
    private String cardType;

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    private List<BindCard> cardList = new ArrayList<>();

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

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getExternalNumber() {
        return externalNumber;
    }

    public void setExternalNumber(String externalNumber) {
        this.externalNumber = externalNumber;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(String successAmount) {
        this.successAmount = successAmount;
    }

    public String getPreBindCode() {
        return preBindCode;
    }

    public void setPreBindCode(String preBindCode) {
        this.preBindCode = preBindCode;
    }

    public List<BindCard> getCardList() {
        return cardList;
    }

    public void setCardList(List<BindCard> cardList) {
        this.cardList = cardList;
    }

    public void addCardList(BindCard bindCard){
        this.cardList.add(bindCard);
    }

//    public boolean isSuccess() {
//        return "0000".equals(responseCode);
//    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Boolean getSuccess(){ return success;}

}
