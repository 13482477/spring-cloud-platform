package com.siebre.product.config;



import java.io.Serializable;

/**
 * @author:chunling.yan
 * @date:15/12/21 下午2:24
 * <p/>
 * Description:
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -4696898674758059398L;
    private String code;
    private String message;
    private T data;
    
    
    
    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(ResultCode resultCode,T t){
        this(resultCode.getCode(),resultCode.getMessage());
        this.data = t;
    }

    public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
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
