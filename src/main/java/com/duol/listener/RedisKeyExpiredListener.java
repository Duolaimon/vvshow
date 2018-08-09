package com.duol.listener;

import com.duol.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 监听redis键过期
 *
 * @author Duolaimon
 * 18-8-4 下午9:26
 */
public class RedisKeyExpiredListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RedisKeyExpiredListener.class);


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
        Object channel = serializer.deserialize(message.getChannel());
        Object body = serializer.deserialize(message.getBody());
        logger.info("主题: {}",channel);
        logger.info("消息内容: {}" , String.valueOf(body));
        String filename = String.valueOf(body).split(":")[1];
        doSthForExpired(filename);
    }

    private void doSthForExpired(String fileName) {
        boolean result = FTPUtil.deleteFile(fileName);
        logger.info("监听器操作结果： {}", result);
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
