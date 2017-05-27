package com.siebre.basic.web;

import java.io.Serializable;

import com.siebre.basic.query.PageInfo;

public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = 4428686798400903636L;

	private PageInfo pageInfo;

	private T data;

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
		private PageInfo pageInfo;
		private T data;

		public Builder<T> pageInfo(PageInfo pageInfo) {
			this.pageInfo = pageInfo;
			return this;
		}

		public Builder<T> data(T data) {
			this.data = data;
			return this;
		}

		public PageResult<T> build() {
			PageResult<T> result = new PageResult<T>();
			result.setPageInfo(this.pageInfo);
			result.setData(this.data);
			return result;
		}
	}

}
