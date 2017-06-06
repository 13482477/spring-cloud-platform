package com.siebre.basic.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.siebre.basic.query.PageInfo;

import java.io.Serializable;

public class WebResult<T> implements Serializable {

	private static final long serialVersionUID = -6145741379535874029L;
	
	public static final String SUCCESS_CODE = "200";

	public static final String FAIL_CODE = "500";

	private String returnCode;

	private String returnMessage;

	@JsonInclude(Include.NON_NULL)
	private PageInfo pageInfo;

	private T data;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static <T> Builder<T> builder() {
		return new Builder<T>();
	}

	public static class Builder<T> {
		private String returnCode;
		private String returnMessage;
		private PageInfo pageInfo;
		private T data;

		public Builder<T> returnCode(String returnCode) {
			this.returnCode = returnCode;
			return this;
		}

		public Builder<T> returnMessage(String returnMessage) {
			this.returnMessage = returnMessage;
			return this;
		}

		public Builder<T> pageInfo(PageInfo pageInfo) {
			this.pageInfo = pageInfo;
			return this;
		}

		public Builder<T> data(T data) {
			this.data = data;
			return this;
		}

		public WebResult<T> build() {
			WebResult<T> result = new WebResult<T>();
			result.setReturnCode(this.returnCode);
			result.setReturnMessage(this.returnMessage);
			result.setPageInfo(this.pageInfo);
			result.setData(this.data);
			return result;
		}
	}

}
