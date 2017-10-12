package com.siebre.basic.sequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by huangfei on 2017/07/11.
 */
public class RedisBasedSequenceGenerator implements SequenceGenerator {

    private static Logger log = LoggerFactory.getLogger(RedisBasedSequenceGenerator.class);

    private RedisTemplate redisTemplate;

    private String sequenceName;

    private static final String SEQUENCE_PREFIX = "siebre_sequence_";

    //TODO Support sequence format
    private String format;

    public RedisBasedSequenceGenerator(RedisTemplate redisTemplate, String sequenceName) {
        this.redisTemplate = redisTemplate;
        this.sequenceName = sequenceName;
    }

    @Override
    public String next() {
        String sequenceNameInReddis = String.format("INCR %s%s", SEQUENCE_PREFIX, sequenceName);
        Long result = (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.incr(sequenceNameInReddis.getBytes());
            }
        });

        if (result == 1l) {
            log.info(String.format("Newly created sequence %s starts from 1", sequenceNameInReddis));
        }

        return null;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
