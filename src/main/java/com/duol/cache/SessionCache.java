package com.duol.cache;

import com.duol.common.Const;
import com.sun.istack.internal.NotNull;
import org.springframework.data.redis.core.ValueOperations;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Duolaimon
 * 18-7-18 下午7:35
 */
public class SessionCache {
    private static RedisCache redisCache = new RedisCache();
    private static ValueOperations<String,String> operations = redisCache.valueOperations();

    public static boolean verifySessionID(String userId, @NotNull String sessionID) {
        String value = operations.get(Const.USER_SESSION_PREFIX + userId);
        return value.equals(sessionID);
    }

    public static String cacheSessionID(String userId) {
        String UUID = getUUID();
        cache(userId,UUID);
        return UUID;
    }

    public static void removeSessionID(String userId) {
        redisCache.getTemplate().delete(Const.USER_SESSION_PREFIX + userId);
    }


    private static void cache(String userId, String uuid) {
        operations.set(Const.USER_SESSION_PREFIX + userId,uuid,30,TimeUnit.MINUTES);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-","").toUpperCase();

    }

    public static boolean verifySessionID(String sessionAttribute) {
        String[] info = sessionAttribute.split("-");
        return verifySessionID(info[0], info[1]);
    }
}
