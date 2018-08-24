package com.duol.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存对象到redis
 * @author Duolaimon
 * 18-7-19 下午4:51
 */
@Service
public class ObjectCache {
    private HashOperations<String, String, String> operations;
    private HashMapper<Object, String, String> hashMapper;

    public static final String REDIS_OBJECT_PREFIX = "object:";

    @Autowired
    public ObjectCache(RedisConnection<String> redisCache) {
        operations = redisCache.hashOperations();
        hashMapper = new DecoratingStringHashMapper<>(new Jackson2HashMapper(true));
    }

    public void cacheObject(String key, Object object) {
        Map<String, String> map = hashMapper.toHash(object);
        operations.putAll(REDIS_OBJECT_PREFIX + key, map);
        operations.getOperations().expire(REDIS_OBJECT_PREFIX + key, 30, TimeUnit.MINUTES);
    }

    public Object entries(String key) {
        Map<String, String> map = operations.entries(REDIS_OBJECT_PREFIX + key);
        return hashMapper.fromHash(map);
    }

    public void deleteCache(String key) {
        operations.getOperations().delete(REDIS_OBJECT_PREFIX + key);
    }

    public String getProperty(String key, String hashKey) {
        return operations.get(REDIS_OBJECT_PREFIX + key, hashKey);
    }

    public void cacheProperty(String key, String hashKey, String hashValue) {
        operations.put(REDIS_OBJECT_PREFIX + key, hashKey, hashValue);
    }

    public boolean hasKey(String key) {
        return operations.getOperations().hasKey(REDIS_OBJECT_PREFIX + key);
    }
}
