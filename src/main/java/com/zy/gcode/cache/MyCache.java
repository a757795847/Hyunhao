package com.zy.gcode.cache;

import com.zy.gcode.pojo.User;
import com.zy.gcode.utils.Du;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Created by admin5 on 17/3/27.
 */
@Component
public class MyCache implements Cache {
    @Autowired
    Jedis jedis;

    private byte[] nameBytes = SerializeUtils.en(getName());

    @Override
    public String getName() {
        return "user_cache";
    }

    @Override
    public Object getNativeCache() {
        return "redis cache";
    }

    @Override
    public ValueWrapper get(Object key) {
        if(key ==null){
            return null;
        }

       if(jedis.hexists(getNameBytes(),SerializeUtils.en(key)))
        return new SimpleValueWrapper(SerializeUtils.de(jedis.hget(getNameBytes(),SerializeUtils.en(key)),Object.class));
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
       ValueWrapper valueWrapper = get(key);
       if(valueWrapper==null){
           return  null;
       }
       Object value = valueWrapper.get();
        return  SerializeUtils.de((byte[])value,type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        if(jedis.hexists(getNameBytes(),SerializeUtils.en(key))){
          return  (T)get(key,Object.class);
        }
        else {
            try {
                Object value = valueLoader.call();
                if(value instanceof Serializable){
                    jedis.hset(getNameBytes(), SerializeUtils.en(key),SerializeUtils.en(value));
                }else {
                    throw new IllegalArgumentException();
                }

                return (T) value;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void put(Object key, Object value) {
        jedis.hset(getNameBytes(),SerializeUtils.en(key),SerializeUtils.en(value));

    }

    private void checkSerialize(Object obj){
        if(!(obj instanceof Serializable)){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        byte[] keyBytes = SerializeUtils.en(key);
        if(jedis.hexists(getNameBytes(),keyBytes)){
            return new SimpleValueWrapper(jedis.hget(getNameBytes(),keyBytes));
        }
        checkSerialize(value);
        jedis.hset(getNameBytes(),keyBytes,SerializeUtils.en(value));
        return null;
    }
    private byte[] getNameBytes(){
        return nameBytes;
    }

    @Override
    public void evict(Object key) {
        byte[] keys = SerializeUtils.en(key);
        if(jedis.hexists(getNameBytes(),keys))
        jedis.hdel(getNameBytes(),keys);
    }

    @Override
    public void clear() {
        jedis.del(getNameBytes());
    }
    public static void main(String[] args){


        Du.pl(Arrays.toString(SerializeUtils.en(new User())));
    }

}
