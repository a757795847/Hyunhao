package com.zy.gcode.security;

import com.zy.gcode.pojo.User;
import com.zy.gcode.pojo.ValidData;
import com.zy.gcode.service.intef.IUserService;
import com.zy.gcode.utils.SubjectUtils;
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

import java.util.Date;
import java.util.List;

/**
 * Created by admin5 on 17/2/15.
 */
public class ZyRealm extends AuthorizingRealm implements InitializingBean{
    Logger log = LoggerFactory.getLogger(ZyRealm.class);

    public static String name;

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
        User user = SubjectUtils.getUser();
        List<ValidData> validDataList = userService.getValidDateList(user.getUsername());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        validDataList.forEach(validData -> {
            if (validData.getEndData().after(new Date())) {
                simpleAuthorizationInfo.addStringPermission(validData.getZyappid().toString());
                log.debug("权限:" + validData.getZyappid());
            }
        });
        simpleAuthorizationInfo.addRole(user.getRole());
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        User user = userService.getUser(token.getPrincipal().toString());
        try {
            user.getUsername();
        } catch (NullPointerException e) {
            log.debug("登录失败！用户名:" + token.getPrincipal());
            throw new UnknownAccountException();
        }
        user.setWechatPublicServerList(userService.getPublicServerList(user.getUsername()));
        userCache.put(user.getUsername(),user);
        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        name = getName();
    }
}
