package com.zy.gcode.cache;

import com.zy.gcode.utils.Du;
import org.springframework.beans.factory.FactoryBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by admin5 on 17/3/27.
 */
public class JedisPoolFactoryBean implements FactoryBean<Jedis>{

    private JedisPool jedisPool = new JedisPool("115.29.188.190");

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
