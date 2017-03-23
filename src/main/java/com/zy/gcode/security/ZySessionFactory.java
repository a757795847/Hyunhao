package com.zy.gcode.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by admin5 on 17/3/21.
 */
public class ZySessionFactory implements SessionFactory {
    private static final String JWT = "jwt";


    @Override
    public Session createSession(SessionContext initData) {
        if(initData instanceof WebSessionContext){
            WebSessionContext  webInitData = (WebSessionContext) initData;
            String jwt =  webInitData.getServletRequest().getParameter("jwt");
            ZySession session = new ZySession();
            session.setId(jwt);
            session.setHost(webInitData.getServletRequest().getRemoteHost());
            return session;
        }
        throw new IllegalStateException("该应用仅支持web环境");
    }

    private String getJwtFromHeader(HttpServletRequest request){
      return request.getHeader(JWT);
    }
}
