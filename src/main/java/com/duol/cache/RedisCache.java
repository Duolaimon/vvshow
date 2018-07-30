package com.duol.cache;

import com.duol.util.PropertiesUtil;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Duolaimon
 * 18-7-18 下午7:50
 */
@Component
public class RedisCache<V> {
    private JedisConnectionFactory redisConnectionFactory;
    private StringRedisTemplate stringRedisTemplate;
    private RedisTemplate<String,V> redisTemplate;

    public RedisCache() {
        redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(PropertiesUtil.getProperty("redis.hostname"));
        redisConnectionFactory.setPort(Integer.valueOf(Objects.requireNonNull(PropertiesUtil.getProperty("redis.port"))));
        redisConnectionFactory.setPassword(PropertiesUtil.getProperty("redis.password"));
        redisConnectionFactory.setUsePool(Boolean.parseBoolean(PropertiesUtil.getProperty("redis.userPool")));
        redisConnectionFactory.afterPropertiesSet();
        stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);

    }

    /**
     *  创建缓存对象列表的 <Code> RedisTemplate </Code>
     */
    void initListTemplate() {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }


    ValueOperations<String, String> valueOperations() {
        return stringRedisTemplate.opsForValue();
    }

    HashOperations<String, String, String> hashOperations() {
        return stringRedisTemplate.opsForHash();
    }

    ListOperations<String, V> listOperations(){
        return redisTemplate.opsForList();
    }

}
