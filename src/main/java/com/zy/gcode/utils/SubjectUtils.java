package com.zy.gcode.utils;

import com.zy.gcode.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.cache.ehcache.EhCacheCache;

/**
 * Created by admin5 on 17/3/29.
 */
public class SubjectUtils {
    private static EhCacheCache userCache;

    public static void setUserCache(EhCacheCache cache){
        userCache =cache;
    }

    public static String getUserName(){
       return (String)SecurityUtils.getSubject().getPrincipal();
    }

    public static User getUser(){
        return userCache.get(getUserName(),User.class);
    }
}
