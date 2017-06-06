package com.siebre.basic.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class ServiceResult<T> implements Serializable {

	private static final long serialVersionUID = -8786653634158459514L;
	
	public static final String SUCCESS_MESSAGE = "successed";

	private Boolean success = Boolean.TRUE;

	private T data;

	private PageInfo pageInfo;

	private String message;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public WebResult<T> convertWebResult() {
		WebResult<T> webResult = new WebResult<T>();
		webResult.setReturnCode(this.success ? "200" : "500");
		webResult.setData(this.data);
		webResult.setPageInfo(this.pageInfo);
		webResult.setReturnMessage(this.getMessage());
		return webResult;
	}

	public static <T> Builder<T> builder() {
		return new Builder<T>();
	}

	public static class Builder<T> {
		
		private Boolean success;
		private T data;
		private PageInfo pageInfo;
		private String message;

		public Builder<T> success(Boolean success) {
			this.success = success;
			return this;
		}

		public Builder<T> data(T data) {
			this.data = data;
			return this;
		}

		public Builder<T> pageInfo(PageInfo pageInfo) {
			this.pageInfo = pageInfo;
			return this;
		}

		public Builder<T> message(String message) {
			this.message = message;
			return this;
		}

		public ServiceResult<T> build() {
			ServiceResult<T> result = new ServiceResult<T>();
			result.setSuccess(this.success);
			result.setData(this.data);
			result.setPageInfo(pageInfo);
			
			if (this.success && StringUtils.isBlank(this.message)) {
				result.setMessage(SUCCESS_MESSAGE);
			}
			else {
				result.setMessage(message);
			}
			
			return result;
		}
	}

}
