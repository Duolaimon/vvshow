package com.duol.cache;

import com.duol.pojo.User;
import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;

/**
 * @author Duolaimon
 * 18-7-19 下午5:29
 */
public class ObjectCacheTest {
    private ObjectCache objectCache;
    String key = "Ftp1:object";

    @Before
    public void setup() {
        objectCache = new ObjectCache(new RedisCache<>());
    }
    @Test
    public void cacheObject() {
        User user = new User();
        user.setId(123213);
        user.setUsername("小贵");
        user.setPhone("42343241");
        user.setEmail("ffdafll432@qq.com");
        objectCache.cacheObject(key,user);
    }

    @Test
    public void entries() {
        User user = (User) objectCache.entries(key);
        System.out.println(user.getUsername());
    }

    @Test
    public void deleteCache() {
        String key = "23";
        objectCache.deleteCache(key);
    }

    @Test
    public void getProperty() {
        String result = objectCache.getProperty("fdaf","fdaf");
        Assert.assertNull(result);
    }
}