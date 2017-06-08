package com.siebre.product.config;

/**
 * 
 * 
 * @version	1.0	2016年7月28日
 * @author	lenovo	
 * @history
 */
public enum ResultCode {
    C200("200", "Success"),
    C403("403", "Forbidden"),
    C500("500", "Internal Server Error"),
    C400("400", "请求参数错误"),
    C402("-402", "用户未登录!"),
	OPERATION_FREQUENCY_TOO_FAST("1000","操作过快,请稍后再试!")
    ;
    
    public String code;
    public String message;
    private ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    
    

}
