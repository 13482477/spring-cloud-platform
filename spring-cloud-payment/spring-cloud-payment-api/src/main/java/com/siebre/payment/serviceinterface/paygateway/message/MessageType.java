package com.siebre.payment.serviceinterface.paygateway.message;



/**
 * 消息类型
 * 
*  @version 1.0 2017年3月14日 
 * @author	
 * @history
 */
public enum MessageType {
    
    merchant("merchant", "from merchant request"),
    channle("channel", "from channel request");

    MessageType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String name;//name
    public String desc;//desc
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
}