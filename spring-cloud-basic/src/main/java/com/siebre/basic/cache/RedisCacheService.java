package com.siebre.basic.cache;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;



public class RedisCacheService implements IRedisCacheService {

	private static Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

	private RedisTemplate<String, Object> redisTemplate;

    private ListOperations<String, Map<String,Object>> listOps;

	private SetOperations<String, Object> setOps;
	
	@Override
	public Object get(String key) {
		try {
			return (Object) redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.error("",e);
			logger.info("Redis get exception, key:{}",key);
			throw new RedisRuntimeException("Redis get exception key:" + key);
		}
	}
	
	public String getStringValue(String key) {
		Object result = get(key);
		if (result == null) {
			return "";
		}
		return String.valueOf(result);
	}

	@Override
	public Object remove(String key) {
		try {
			redisTemplate.delete(key);
			logger.info("Redis remove {} success!",key);
			return null;
		} catch (Exception e) {
			logger.error("",e);
			logger.info("Redis remove exception, key{}",key);
			throw new RedisRuntimeException("Redis remove exception key:" + key);
		}
	}
	
	@Override
	public void delete(String key) {
		try {
			redisTemplate.delete(key);
			logger.info("Redis delete {} success!",key);
		} catch (Exception e) {
			logger.error("",e);
			logger.info("Redis delete exception, key{}",key);
			throw new RedisRuntimeException("Redis remove delete key:" + key);
		}
	}

	/**
	 */
	@Override
	public void put(String key, Object value, int durable) {
		try {
			redisTemplate.opsForValue().set(key, value, durable, TimeUnit.SECONDS);
			if (logger.isDebugEnabled()) {
				logger.debug("Redis put key:{} value:{}",new Object[]{key,value});
			}
		} catch (Exception e) {
			logger.error("",e);
			logger.info("Redis put exception, key:{},value:{}",new Object[]{key,value});
			throw new RedisRuntimeException("Redis put exception key:" + key);
		}
	}

	/**
	 */
	@Override
	public void putDefault(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value, DateUtils.MILLIS_PER_DAY, TimeUnit.MILLISECONDS);
			if (logger.isDebugEnabled()) {
				logger.debug("Redis put key:{} value:{}",new Object[]{key,value});
			}
		} catch (Exception e) {
			logger.error("",e);
			logger.info("Redis put exception, key:{},value:{}",new Object[]{key,value});
			throw new RedisRuntimeException("Redis putDefault exception key:" + key);
		}
	}

	@Override
	public void putForever(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key,  value);
			if (logger.isDebugEnabled()) {
				logger.debug("Redis put key:{} value:{}", new Object[]{key,value});
			}
		} catch (Exception e) {
			logger.error("",e);
			logger.info("Redis put exception, key:{},value:{}",new Object[]{key,value});
			throw new RedisRuntimeException("Redis putForever exception key:" + key);
		}
	
	}

	@Override
	public void clear() { // NOSONAR
	}
	
	@Override
	public Set<String> keys(String keyPattern) {
		try {
			return redisTemplate.keys(keyPattern);
		} catch (Exception e) {
			logger.error("Redis keys exception..",e);
			throw new RedisRuntimeException("Redis keys exception");
		}
	}
	
	@Override
	public void delete(Set<String> keys) {
		if (keys == null) {
			return;
		}
		try {
			redisTemplate.delete(keys);
			logger.info("Redis delete total {} success!", keys.size());
		} catch (Exception e) {
			logger.error("Redis delete exception..",e);
			throw new RedisRuntimeException("Redis delete exception");
		}
	}
	
	public Long putListMap(String key, List<Map<String, Object>> list) {
		return listOps.rightPushAll(key, list);
	}
	
	public List<Map<String, Object>> getListMap(String key, long start, long end) {
		return listOps.range(key, start, end);
	}


	@Override
	public Long addToSet(String key, Object... values){
		return setOps.add(key,values);
	}

	@Override
	public Set getSetMembers(String key){
		SetOperations setOps = redisTemplate.opsForSet();
		return setOps.members(key);
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public ListOperations<String, Map<String, Object>> getListOps() {
		return listOps;
	}

	public void setListOps(ListOperations<String, Map<String, Object>> listOps) {
		this.listOps = listOps;
	}

	public SetOperations<String, Object> getSetOps() {
		return setOps;
	}

	public void setSetOps(SetOperations<String, Object> setOps) {
		this.setOps = setOps;
	}
}
