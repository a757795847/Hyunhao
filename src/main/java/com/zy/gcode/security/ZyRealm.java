package com.zy.gcode.security;

import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.WxOperator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin5 on 17/2/15.
 */
public class ZyRealm extends AuthorizingRealm {
    Logger log = LoggerFactory.getLogger(ZyRealm.class);

    PersistenceService persistenceService;


    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
      WxOperator wxOperator = persistenceService.get(WxOperator.class,token.getPrincipal().toString());
        try {
         wxOperator.getUsername();
        } catch (NullPointerException e) {
            throw new UnknownAccountException();
        }
          return   new SimpleAuthenticationInfo(wxOperator.getUsername(),wxOperator.getPassword(),getName());

    }

}
