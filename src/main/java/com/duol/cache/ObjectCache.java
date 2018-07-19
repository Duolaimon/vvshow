package com.duol.cache;

import com.duol.common.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Duolaimon
 * 18-7-19 下午4:51
 */
@Service
public class ObjectCache {
    private RedisCache redisCache;
    private HashOperations<String, String, String> operations;
    private HashMapper<Object, String, String> hashMapper;

    @Autowired
    public ObjectCache() {
        operations = redisCache.hashOperations();
        hashMapper = new DecoratingStringHashMapper<>(new Jackson2HashMapper(true));
    }

    public void cacheObject(String key, Object object) {
        key = Const.REDIS_OBJECT_PREFIX + key;
        Map<String, String> map = hashMapper.toHash(object);
        operations.putAll(key, map);
        operations.getOperations().expire(key, 30, TimeUnit.MINUTES);
    }

    public Object entries(String key) {
        Map<String, String> map = operations.entries(Const.REDIS_OBJECT_PREFIX + key);
        return hashMapper.fromHash(map);
    }

    public void deleteCache(String key) {
        redisCache.getTemplate().delete(Const.REDIS_OBJECT_PREFIX + key);
    }

}
