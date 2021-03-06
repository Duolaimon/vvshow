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
public class ValueCache {
    private static RedisConnection<String> redisCache = new RedisConnection<>();
    private static ValueOperations<String, String> operations = redisCache.valueOperations();
    private static final String USER_SESSION_PREFIX = "session:";

    public static final String USERNAME_PREFIX = "username:";
    public static final String TOKEN_PREFIX = "token:";
    public static final String QR_CODE_PREFIX = "qr-code:";

    public static boolean verifySessionID(String userId, @NotNull String sessionID) {
        String value = operations.get(USER_SESSION_PREFIX + userId);
        return sessionID.equals(value);
    }

    public static boolean verifyManageSessionId(String userId, @NotNull String sessionID) {
        if (verifySessionID(userId, sessionID)) {
            ObjectCache objectCache = new ObjectCache(redisCache);
            return Const.Role.ROLE_ADMIN.toString().equals(objectCache.getProperty(userId, "role"));
        }
        return false;
    }

    public static String cacheSessionID(String userId) {
        String UUID = getUUID();
        cache(USER_SESSION_PREFIX + userId, UUID);
        return UUID;
    }

    /**
     * 延长sessionId时长
     */
    public static void expireSessionId(String userId) {
        expire(USER_SESSION_PREFIX + userId, 12, TimeUnit.HOURS);
    }

    public static void removeSessionID(String userId) {
        delete(USER_SESSION_PREFIX + userId);
    }


    public static void cache(String key, String uuid) {
//        operations.set(key, uuid, 30, TimeUnit.MINUTES);
        operations.set(key, uuid, 30, TimeUnit.MINUTES);
    }

    public static void expire(String key, long timeout, TimeUnit unit) {
        operations.getOperations().expire(key, timeout, unit);
    }

    public static String get(String key) {
        return operations.get(key);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();

    }

    public static void delete(String key) {
        operations.getOperations().delete(key);
    }
}
