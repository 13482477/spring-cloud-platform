package com.siebre.basic.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 分页对象
 * @author john.lee
 */
public class PageInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 每页显示数据行数
	 */
	private int showCount = 10;
	
	/**
	 * 总页数
	 */
	private int totalPage;

	private int offset;
	
	/**
	 * 总记录数
	 */
	private int totalResult; 
	
	/**
	 * 当前页
	 */
	private int currentPage = 1;
	
	/**
	 * 起始行
	 */
	private int currentResult;

	/**
	 * 排序字段
	 */
	private String sortField;
	
	/**
	 * 升序或降序
	 */
	private String order;

	public int getOffset() {
		this.offset = (currentPage -1) * showCount;
		if(offset <0){
			offset = 0;
		}

		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	private Map<String, Object> searchParameters = new HashMap<String, Object>();
	
	public PageInfo() {
	}
	
	public PageInfo(int page, int pageCount) {
		super();
		this.currentPage = page;
		this.showCount = pageCount;
	}
	
	public PageInfo(int showCount, int currentPage, String sortField, String order) {
		this.showCount = showCount;
		this.currentPage = currentPage;
		this.sortField = sortField;
		this.order = order;
	}

	public int getShowCount() {
		return showCount;
	}

	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}

	public int getTotalPage() {
		totalPage = (totalResult + showCount - 1) / showCount;
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getCurrentResult() {
		currentResult = (currentPage - 1) * showCount;
		return currentResult;
	}

	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Map<String, Object> getSearchParameters() {
		return searchParameters;
	}

	public void setSearchParameters(Map<String, Object> searchParameters) {
		this.searchParameters = searchParameters;
	}
}
