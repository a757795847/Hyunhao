package com.zy.gcode.security;

import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.WxOperator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by admin5 on 17/2/15.
 */
public class ZyRealm extends AuthorizingRealm {
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
      String password =  String.valueOf((char[]) token.getCredentials());
        try {
         wxOperator.getUsername();
        } catch (NullPointerException e) {
            throw new UnknownAccountException();
        }
        if(!password.equals(wxOperator.getPassword())) {
            throw new IncorrectCredentialsException();
        }
        SecurityUtils.getSubject().getSession().setAttribute("operator",wxOperator);
        return new SimpleAuthenticationInfo(token.getPrincipal(),token.getCredentials(),getName());
    }
}
