package com.siebre.payment.serviceinterface.paygateway.message;

import java.io.Serializable;


/**
 * 
 * Title: Class Message
 * Description:
 *	报文消息类
 *
 * @author chunling.yan
 * @email    
*  @version 1.0 2017年3月14日 
 */
public class Message implements Serializable{
    
    
	private static final long serialVersionUID = -8152058901586637837L;

	/**
     * 请求编号
     */
    private String reqestNo;

    /**
     * 请求报文体
     */
    private String body;
    
    /**
     * 消息类型
     */
    private MessageType type;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 请求编码
     */
    private String charset;
    
    /**
     * 创建者
     */
    private String creator;

    public String getReqestNo() {
        return reqestNo;
    }

    public void setReqestNo(String reqestNo) {
        this.reqestNo = reqestNo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Message [reqestNo=" + reqestNo + ", body=" + body + ", type=" + type + ", status="
                + status + ", charset=" + charset + ", creator=" + creator + "]";
    }
    

}
