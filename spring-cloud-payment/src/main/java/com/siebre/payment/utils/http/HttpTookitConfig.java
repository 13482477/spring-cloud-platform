/**
 * Created on 2016年10月12日
 * filename: HttpTookitConfig.java
 * Description: 
 *
 */
package com.siebre.payment.utils.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Title: Class HttpTookitConfig
 * Description:
 *	http tookit config
 *	
 *  
 * @email    
*  @version 1.0 2017年3月14日 
 */
public class HttpTookitConfig {

	
	
	private static final String DEFAULT_CHARSET = "UTF-8";
	
	/** 
	 * 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒
	 */
	private static final long DEFAULT_HTTPCONNECTIONMANAGER_TIMEOUT = 3 * 1000;

	/** 
	 * 等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒
	 */
	private long connManagerTimeout = DEFAULT_HTTPCONNECTIONMANAGER_TIMEOUT;
	
	
	private static final Map<String, String> DEFAULT_HEAD_MAP = new HashMap<>(2);
	static{
		DEFAULT_HEAD_MAP.put("Content-Type", "application/x-www-form-urlencoded; text/html; charset=utf-8");
		DEFAULT_HEAD_MAP.put("User-Agent", "Mozilla/4.0");
	}
	
	/**
	 * http head map
	 */
	private Map<String, String> headMap = DEFAULT_HEAD_MAP;

	/**
	 * 编码
	 */
	private String charset = DEFAULT_CHARSET;

	/**
	 *  连接超时时间，缺省为2秒钟 
	 */
	private int defaultConnectionTimeout = 5*1000;

	/**
	 *  读取数据超时，缺省为2秒钟
	 */
	private int defaultSoTimeout = 5*1000;

	/**
	 * 从连接池获取连接时间的，默认500毫秒
	 */
	private int connectionRequestTimeout = 500;
	
	/** 
	 * 闲置连接超时时间, 由bean factory设置，缺省为60秒钟
	 */
	private int defaultIdleConnTimeout = 2*1000;
	

	/**
	 * 每个链路最大连接数
	 */
	private int defaultMaxConnPerHost = 200;

	/**
	 * 默认最大连接数
	 */
	private int defaultMaxTotalConn = 1000;

	public long getConnManagerTimeout() {
		return connManagerTimeout;
	}

	public void setConnManagerTimeout(long connManagerTimeout) {
		this.connManagerTimeout = connManagerTimeout;
	}


	public Map<String, String> getHeadMap() {
		return headMap;
	}

	public void setHeadMap(Map<String, String> headMap) {
		this.headMap = headMap;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getDefaultConnectionTimeout() {
		return defaultConnectionTimeout;
	}

	public void setDefaultConnectionTimeout(int defaultConnectionTimeout) {
		this.defaultConnectionTimeout = defaultConnectionTimeout;
	}

	public int getDefaultSoTimeout() {
		return defaultSoTimeout;
	}

	public void setDefaultSoTimeout(int defaultSoTimeout) {
		this.defaultSoTimeout = defaultSoTimeout;
	}

	public int getDefaultIdleConnTimeout() {
		return defaultIdleConnTimeout;
	}

	public void setDefaultIdleConnTimeout(int defaultIdleConnTimeout) {
		this.defaultIdleConnTimeout = defaultIdleConnTimeout;
	}

	public int getDefaultMaxConnPerHost() {
		return defaultMaxConnPerHost;
	}

	public void setDefaultMaxConnPerHost(int defaultMaxConnPerHost) {
		this.defaultMaxConnPerHost = defaultMaxConnPerHost;
	}

	public int getDefaultMaxTotalConn() {
		return defaultMaxTotalConn;
	}

	public void setDefaultMaxTotalConn(int defaultMaxTotalConn) {
		this.defaultMaxTotalConn = defaultMaxTotalConn;
	}

	
}
