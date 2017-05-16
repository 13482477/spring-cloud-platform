package com.siebre.payment.cache;

import java.io.Serializable;


public interface ICacheService extends Serializable {
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key);

	/**
	 * 
	 * @param key
	 */
	public Object remove(String key);

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value, int durable);

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putDefault(String key, Object value);
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putForever(String key, Object value);


	/**
	 * 
	 */
	public void clear();

}
