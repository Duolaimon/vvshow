package com.duol.cache;

import com.duol.util.PropertiesUtil;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Duolaimon
 * 18-7-18 下午7:50
 */
@Component
public class RedisCache {
    private StringRedisTemplate template;

    public RedisCache() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(PropertiesUtil.getProperty("redis.hostname"));
        redisConnectionFactory.setPort(Integer.valueOf(Objects.requireNonNull(PropertiesUtil.getProperty("redis.port"))));
        redisConnectionFactory.setPassword(PropertiesUtil.getProperty("redis.password"));
        redisConnectionFactory.setUsePool(Boolean.parseBoolean(PropertiesUtil.getProperty("redis.userPool")));
        redisConnectionFactory.afterPropertiesSet();
        template = new StringRedisTemplate(redisConnectionFactory);
        template.afterPropertiesSet();
    }

    public StringRedisTemplate getTemplate() {
        return template;
    }

    public ValueOperations<String, String> valueOperations() {
        return template.opsForValue();
    }

    public HashOperations<String, String, String> hashOperations() {
        return template.opsForHash();
    }

}
