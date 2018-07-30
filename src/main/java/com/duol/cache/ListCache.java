package com.duol.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Duolaimon
 * 18-7-23 上午11:55
 */
@Service
public class ListCache<V> {
    private ListOperations<String,V> operations;

    private static final String REDIS_LIST_PREFIX = "list:";

    @Autowired
    public ListCache(RedisCache<V> redisCache) {
        redisCache.initListTemplate();
        operations = redisCache.listOperations();
    }


    public boolean hasKey(String key) {
        return operations.getOperations().hasKey(REDIS_LIST_PREFIX + key);
    }

    public void cacheNewList(String key, List<V> list) {
        delete(key);
        operations.leftPushAll(REDIS_LIST_PREFIX + key, list);
        operations.getOperations().expire(REDIS_LIST_PREFIX + key, 8, TimeUnit.HOURS);
    }

    public List<V> range(String key) {
        return operations.range(REDIS_LIST_PREFIX + key, 0, -1);
    }

    public void delete(String key) {
        operations.getOperations().delete(REDIS_LIST_PREFIX + key);
    }
}
