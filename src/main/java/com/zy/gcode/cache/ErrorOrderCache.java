package com.zy.gcode.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

/**
 * Created by admin5 on 17/4/5.
 */
@Component
public class ErrorOrderCache extends RedisCache{
    @Autowired
    public ErrorOrderCache(RedisOperations<?, ?> redisOperations) {
        super("errorList", "errorList".getBytes(), redisOperations, 1000);
    }
}
