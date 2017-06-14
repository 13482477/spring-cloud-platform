package com.siebre.product.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.siebre.basic.cache.RedisCacheService;

@Configuration
public class RedisConfig {

	@Bean("sessionRedisSentinelConfiguration")
	@Autowired
	public RedisSentinelConfiguration sessionRedisSentinelConfiguration(Environment env) {
		RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
		
		RedisNode node = new RedisNode("", 11111);
		node.setName("mymaster");
		configuration.setMaster(node);
		
		RedisNode node1 = new RedisNode(env.getProperty("session.redis.sentinels.host1"), env.getProperty("session.redis.sentinels.port1", Integer.class));
		RedisNode node2 = new RedisNode(env.getProperty("session.redis.sentinels.host2"), env.getProperty("session.redis.sentinels.port2", Integer.class));
		RedisNode node3 = new RedisNode(env.getProperty("session.redis.sentinels.host3"), env.getProperty("session.redis.sentinels.port3", Integer.class));
		
		Set<RedisNode> sentinels = new HashSet<RedisNode>(){{
			add(node1);
			add(node2);
			add(node3);
		}};
		
		configuration.setSentinels(sentinels);
		
		return configuration;
	}
	
	@Bean("cacheRedisSentinelConfiguration")
	@Autowired
	public RedisSentinelConfiguration cacheRedisSentinelConfiguration(Environment env) {
		RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
		
		RedisNode node = new RedisNode("", 11111);
		node.setName("mymaster");
		configuration.setMaster(node);
		
		RedisNode node1 = new RedisNode(env.getProperty("cache.redis.sentinels.host1"), env.getProperty("cache.redis.sentinels.port1", Integer.class));
		RedisNode node2 = new RedisNode(env.getProperty("cache.redis.sentinels.host2"), env.getProperty("cache.redis.sentinels.port2", Integer.class));
		RedisNode node3 = new RedisNode(env.getProperty("cache.redis.sentinels.host3"), env.getProperty("cache.redis.sentinels.port3", Integer.class));
		
		Set<RedisNode> sentinels = new HashSet<RedisNode>(){{
			add(node1);
			add(node2);
			add(node3);
		}};
		
		configuration.setSentinels(sentinels);
		
		return configuration;
	}
	
	@Bean("sessionJedisConnectionFactory")
	@Autowired
	public JedisConnectionFactory sessionJedisConnectionFactory(Environment env,  @Qualifier("sessionRedisSentinelConfiguration") RedisSentinelConfiguration configuration) {
		JedisConnectionFactory factory = new JedisConnectionFactory(configuration);
		factory.setUsePool(true);
		factory.setDatabase(Integer.parseInt(env.getProperty("session.redis.database")));
		
		return factory;
	}
	
	@Bean("cacheJedisConnectionFactory")
	@Autowired
	public JedisConnectionFactory cacheJedisConnectionFactory(Environment env,  @Qualifier("cacheRedisSentinelConfiguration") RedisSentinelConfiguration configuration) {
		JedisConnectionFactory factory = new JedisConnectionFactory(configuration);
		factory.setUsePool(true);
		factory.setDatabase(Integer.parseInt(env.getProperty("cache.redis.database")));
		
		return factory;
	}
	
	@Bean("redisTemplate")
	@Autowired
	public RedisTemplate redisTemplate(@Qualifier("cacheJedisConnectionFactory") JedisConnectionFactory connectionFactory) {
		RedisTemplate template = new RedisTemplate();
		template.setConnectionFactory(connectionFactory);
		
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		
		return template;
	}
	
	@Bean
	@Autowired
	public RedisCacheService redisCacheService(RedisTemplate redisTemplate) {
		RedisCacheService cacheService = new RedisCacheService();
		
		cacheService.setRedisTemplate(redisTemplate);
		
		ListOperations<String, Map<String, Object>> listOps = redisTemplate.opsForList();
		SetOperations<String, Object> setOps = redisTemplate.opsForSet();
		
		cacheService.setListOps(listOps);
		cacheService.setSetOps(setOps);
		
		return cacheService;
	}
	
}
