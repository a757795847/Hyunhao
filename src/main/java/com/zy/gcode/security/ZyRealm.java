package com.zy.gcode.security;

import com.zy.gcode.pojo.User;
import com.zy.gcode.service.intef.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheCache;

/**
 * Created by admin5 on 17/2/15.
 */
public class ZyRealm extends AuthorizingRealm implements InitializingBean {
    public static String name;
    Logger log = LoggerFactory.getLogger(ZyRealm.class);
    @Autowired
    @Qualifier("userCache")
    EhCacheCache userCache;

    IUserService userService;

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }


    @Autowired
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        super.setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        userService.setRole(authorizationInfo);
        userService.setUserAuthority(authorizationInfo);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        User user = userCache.get(token.getPrincipal().toString(),User.class);
        if(user==null){
            userService.getUser(token.getPrincipal().toString());
        }
        if(user==null){
            throw new UnknownAccountException();
        }
        userCache.put(user.getUsername(), user);
        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        name = getName();
    }
}
