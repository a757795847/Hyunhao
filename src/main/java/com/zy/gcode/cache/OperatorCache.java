package com.zy.gcode.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

/**
 * Created by admin5 on 17/3/28.
 */
@Component
public class OperatorCache extends RedisCache {

    @Autowired
    public OperatorCache(RedisOperations<?, ?> redisOperations) {
        super("user", "user".getBytes(), redisOperations, 1000);
    }
}
