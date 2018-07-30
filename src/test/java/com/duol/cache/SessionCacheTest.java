package com.duol.cache;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author Duolaimon
 * 18-7-18 下午7:40
 */
public class SessionCacheTest {
    StringRedisTemplate template;
    @Before
    public void setup() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
//        redisConnectionFactory.setHostName(PropertiesUtil.getProperty("redis.hostname"));
//        redisConnectionFactory.setPort(Integer.valueOf(Objects.requireNonNull(PropertiesUtil.getProperty("redis.port"))));
//        redisConnectionFactory.setPassword(PropertiesUtil.getProperty("redis.password"));
//        redisConnectionFactory.setUsePool(Boolean.parseBoolean(PropertiesUtil.getProperty("redis.userPool")));
        JedisConnectionFactory connectionFactory;
        connectionFactory = new JedisConnectionFactory();
        connectionFactory.setUsePool(true);
        connectionFactory.setHostName("127.0.0.1");
        connectionFactory.setPort(6379);
        connectionFactory.setPassword("123456");
        connectionFactory.afterPropertiesSet();
        template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
    }

    @Test
    public void getUUID() {
        System.out.println(ValueCache.getUUID());
    }

    @Test
    public void cacheSessionID() {
        String sessionId = ValueCache.cacheSessionID("阿里");
        System.out.println(sessionId);
    }

    @Test
    public void templateTest() {
        ValueOperations operations = template.opsForValue();
        operations.set("operation","operation");
    }
}