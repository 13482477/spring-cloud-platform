package com.siebre.payment.serviceinterface.paygateway.result;

import java.io.Serializable;

/**
 * 
 * Title: Class GateWayResult
 * Description:
 *	交易返回结果对象
 *
 * @author chunling.yan
 * @email    
*  @version 1.0 2017年3月14日 
 * @param <T>
 */
public final class GateWayResult<T> implements Serializable {
	
	private static final long serialVersionUID = 895564619175921346L;
	
	/**
	 *  return code
	 */
	private String code;
	
	/**
	 * return message
	 */
	private String message;
	
	/**
	 * operation flag
	 */
	private boolean success;
	
	/**
	 * return object
	 */
	private T value;

	
	/**
	 * construction method
	 * @param code
	 * 	return code
	 * @param message
	 * 	return message
	 * @param success
	 * 	operation flag
	 * @param value
	 * 	return object
	 */
	protected GateWayResult(String code, String message, boolean success, T value) {
		this.code = code;
		this.message = message;
		this.success = success;
		this.value = value;
	}

	/**
	 * 
	 * 构建返回对象实例
	 * Created on 2016年10月12日
	 * @author chunling.yan
*  @version 1.0 2017年3月14日 
	 * @param resultCode
	 * @param success
	 * @param value
	 * @return
	 */
	public static <T> GateWayResult<T> newInstance(
			GateWayResultCode resultCode, boolean success, T value) {
		return new GateWayResult<T>(resultCode.code, resultCode.message,
				success, value);
	}

	/**
	 * 
	 * 构建成功对象实例
	 * Created on 2016年10月12日
	 * @author chunling.yan
*  @version 1.0 2017年3月14日 
	 * @return
	 */
	public static <T> GateWayResult<T> newSuccess() {
		return new GateWayResult<T>(GateWayResultCode.SUCCESS.code,
				GateWayResultCode.SUCCESS.message, true, null);
	}

	/**
	 * 
	 *	构建错误对象
	 * Created on 2016年10月12日
	 * @author chunling.yan
*  @version 1.0 2017年3月14日 
	 * @param errorCode
	 * @return
	 */
	public static <T> GateWayResult<T> newError(GateWayResultCode errorCode) {
		return new GateWayResult<T>(errorCode.code, errorCode.message, true,
				null);
	}

	/**
	 * 
	 * 设置错误编码
	 * Created on 2016年10月12日
	 * @author chunling.yan
*  @version 1.0 2017年3月14日 
	 * @param errorCode
	 */
	public  GateWayResult<T> setErrorCode(GateWayResultCode errorCode){
		this.code = errorCode.code;
		this.message = errorCode.message;
		this.success = false;
		return this;
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

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "GateWayResult [code=" + code + ", message=" + message
				+ ", success=" + success + ", value=" + value + "]";
	}

}
