package com.zy.gcode.security;

import com.zy.gcode.pojo.User;
import com.zy.gcode.pojo.ValidData;
import com.zy.gcode.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by admin5 on 17/2/15.
 */
@Component
public class ZyRealm extends AuthorizingRealm {
    Logger log = LoggerFactory.getLogger(ZyRealm.class);


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
        User user = (User) principals.getPrimaryPrincipal();
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
        SecurityUtils.getSubject().getSession(true).setAttribute("operator", user);
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());

    }

}
