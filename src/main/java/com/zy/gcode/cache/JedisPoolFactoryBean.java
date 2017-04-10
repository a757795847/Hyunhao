package com.zy.gcode.cache;

import com.zy.gcode.utils.Du;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

/**
 * Created by admin5 on 17/3/27.
 */
public class JedisPoolFactoryBean implements FactoryBean<Jedis> {
    //foobared123456
    private JedisPool jedisPool = new JedisPool(new GenericObjectPoolConfig(), "localhost", 6388, Protocol.DEFAULT_TIMEOUT,
            "foobared123456", Protocol.DEFAULT_DATABASE, null);

    @Override
    public Jedis getObject() throws Exception {
        Du.dPl("调用了getResource()方法");
        return jedisPool.getResource();
    }

    @Override
    public Class<?> getObjectType() {
        return Jedis.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
