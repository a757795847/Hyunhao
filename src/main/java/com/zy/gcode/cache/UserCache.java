package com.zy.gcode.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by admin5 on 17/3/27.
 */
@Component
public class UserCache implements Cache {
    @Autowired
    Jedis jedis;

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
        if(key instanceof  String){
           return getString(key);
        }
        return null;
    }

    private ValueWrapper getString(Object key){
       if(isExistByUserCache(key))
           return getByUserCache(key);
       return null;
    }

    private ValueWrapper getByUserCache(Object key){
        if(key ==null){
            return new SimpleValueWrapper(jedis.hget(getName(),"null"));
        }
        return new SimpleValueWrapper(jedis.hget(getName(),key.toString()));
    }

    private boolean isExistByUserCache(Object key){
        if(key==null){
           return jedis.hexists(getName(),"null");
        }
      return   jedis.hexists(getName(),key.toString());
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
       ValueWrapper valueWrapper = get(key);
        return (T)valueWrapper.get();
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        if(isExistByUserCache(key)){
          return  (T)getByUserCache(key);
        }
        else {
            try {
                Object value = valueLoader.call();
                jedis.hset(getName(),String.valueOf(key),(String)value);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }
}
