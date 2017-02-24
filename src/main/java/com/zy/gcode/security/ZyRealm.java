package com.zy.gcode.security;

import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.User;
import com.zy.gcode.pojo.ValidData;
import com.zy.gcode.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
       User user = (User)principals.getPrimaryPrincipal();
       List<ValidData> validDataList = userService.getValidDateList(user.getUsername());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        validDataList.forEach(validData -> {
            if(validData.getEndData().after(new Date())){
                simpleAuthorizationInfo.addStringPermission("zyappid"+validData.getZyappid().toString());
            }
        });
        return simpleAuthorizationInfo;
    }

    @Override
    @Transactional(readOnly = true)
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        User user = userService.getUser(token.getPrincipal().toString());
        try {
            user.getUsername();
        } catch (NullPointerException e) {
            throw new UnknownAccountException();
        }
        System.out.println(user.getName());
        SecurityUtils.getSubject().getSession(true).setAttribute("operator", user);
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());

    }

}
