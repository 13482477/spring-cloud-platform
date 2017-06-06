package com.siebre.payment.paymenthandler.baofoo.pay.prepay;

/**
 * Created by meilan on 2017/5/27.
 * 交易参数配置
 */
public class BaofooCon {
    public String terminalId = "34424";

    public String version = "4.0.1.0";

    public String inputCharset = "1";

    public String dataType = "xml";

    public String pfxPassword = "123456";

    public String accNo = "6214850218622493";

    public String cardHolder = "蒋美兰";

    public String idHolder = "蒋美兰";

    public String idCardType = "01";

    public String idCard = "320481199211085223";

    public String mobile = "15501520762";

    public String userId = "123456789";

    public String payCode = "CMB";

    public String payCm = "1";

    public String txnType = "0431";

    public String txnSubType = "13";

    public String bizType = "0000";

    //风控参数信息
    public String goodsCategory = "2005";

    public String userloginId = "保联网络科技有限公司";

    public String identifyState = "0";

    public String getPayCm() {
        return payCm;
    }

    public void setPayCm(String payCm) {
        this.payCm = payCm;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public String getUserloginId() {
        return userloginId;
    }

    public void setUserloginId(String userloginId) {
        this.userloginId = userloginId;
    }

    public String getIdentifyState() {
        return identifyState;
    }

    public void setIdentifyState(String identifyState) {
        this.identifyState = identifyState;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getPfxPassword() {
        return pfxPassword;
    }

    public void setPfxPassword(String pfxPassword) {
        this.pfxPassword = pfxPassword;
    }

    public String getIdHolder() {
        return idHolder;
    }

    public void setIdHolder(String idHolder) {
        this.idHolder = idHolder;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
}


