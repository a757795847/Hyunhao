package com.zy.gcode.utils;

import com.zy.gcode.pojo.User;
import com.zy.gcode.service.intef.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.cache.ehcache.EhCacheCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin5 on 17/3/29.
 */
public class SubjectUtils {
    private static EhCacheCache userCache;
    private static IUserService userService;

    public static void setUserCache(EhCacheCache cache) {
        userCache = cache;
    }

    public static void setUserService(IUserService userService) {
        SubjectUtils.userService = userService;
    }

    public static String getUserName() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

    public static User getUser() {
        User user = userCache.get(getUserName(), User.class);
        if (user == null) {
            user = userService.getUser(getUserName());
        }
        if (user == null) {
            throw new IllegalArgumentException("用户名在数据库中不存在！");
        }
        return user;
    }

    public static void updateUser(User user) {
        userCache.put(getUserName(), user);
    }

    public static boolean isAnonymous() {
        return getUserName().startsWith("anonymous");
    }

    public static HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) ((WebDelegatingSubject) SecurityUtils.getSubject()).getServletResponse();
    }

    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) ((WebDelegatingSubject) SecurityUtils.getSubject()).getServletRequest();
    }
}
