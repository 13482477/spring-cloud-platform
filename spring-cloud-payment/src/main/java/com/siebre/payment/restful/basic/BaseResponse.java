package com.siebre.payment.restful.basic;

import java.io.Serializable;

import com.siebre.basic.query.PageInfo;

public class BaseResponse<T> implements Serializable {

	private static final long serialVersionUID = 498374221947138948L;

	private String returnCode;

	private String message;

	private T data;

	private PageInfo page;

	public BaseResponse() {
	}
	
	public BaseResponse(String returnCode, String message) {
		this.returnCode = returnCode;
		this.message = message;
		this.data = null;
		this.page = null;
	}
	
	public BaseResponse(String returnCode, String message, T data) {
		this.returnCode = returnCode;
		this.message = message;
		this.data = data;
		this.page = null;
	}

	public BaseResponse(String returnCode, String message, T data, PageInfo page) {
		this.returnCode = returnCode;
		this.message = message;
		this.data = data;
		this.page = page;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public PageInfo getPage() {
		return page;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}

	public static <T> BaseResponse<T> FAIL(String s) {
		return new BaseResponse<T>(ReturnCode.FAIL.getValue(), s, null, null);
	}

	public static <T> BaseResponse<T> SUCCESS(String s) {
		return new BaseResponse<T>(ReturnCode.SUCCESS.getValue(), s, null, null);
	}

	public static <T> BaseResponse<T> SUCCESS(T data) {
		return new BaseResponse<T>(ReturnCode.SUCCESS.getValue(), null, data, null);
	}

	public static <T> BaseResponse<T> SUCCESS(String s, T data) {
		return new BaseResponse<T>(ReturnCode.SUCCESS.getValue(), s, data, null);
	}

	public static <T> BaseResponse<T> SUCCESS(T data, PageInfo page) {
		return new BaseResponse<T>(ReturnCode.SUCCESS.getValue(), null, data, page);
	}
}
