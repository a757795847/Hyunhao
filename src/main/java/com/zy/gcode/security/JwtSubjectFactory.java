package com.zy.gcode.security;

import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.Claim;
import com.zy.gcode.utils.JwtUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by admin5 on 17/3/22.
 */
public class JwtSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context) {
        if (!(context instanceof WebSubjectContext)) {
            return super.createSubject(context);
        }
        WebSubjectContext webContext = (WebSubjectContext) context;
        if (webContext.isAuthenticated()) {
            return super.createSubject(context);
        }
        HttpServletRequest request = WebUtils.toHttp(webContext.getServletRequest());
        return jwtSubject(request, (HttpServletResponse) webContext.getServletResponse(), context.getSecurityManager());
    }

    private Subject jwtSubject(HttpServletRequest request, HttpServletResponse response, SecurityManager securityManager) {
        SimpleSession session = new SimpleSession();
        PrincipalCollection principals = null;
        boolean authenticated = false;
        String host = request.getRemoteHost();
        String authorization;
        authorization = request.getHeader("authorization");
        Map map = JwtUtils.deJwtWithTwo(authorization==null?null:authorization.substring("bearer ".length()));
        boolean sessionEnabled = false;
        if (map != null && map.containsKey(PublicClaims.SUBJECT)) {
            session.setAttributes(map);
            String userName = ((Claim) map.get(PublicClaims.SUBJECT)).asString();
            principals = new SimplePrincipalCollection(userName, ZyRealm.name);
        }
        if (principals != null) {
            authenticated = true;
        }
        return new WebDelegatingSubject(principals, authenticated, host, session, sessionEnabled,
                request, response, securityManager);
    }

/*
    private Map getClaimMap(String token) {
        if (token == null) {
            return null;
        }
       token = token.substring("bearer ".length());

        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256("secret");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JWTVerifier verifier; //Reusable verifier instance
        try {
            verifier = JWT.require(algorithm)
                    .build();
        } catch (Exception e) {
            return null;
        }
        DecodedJWT jwt = verifier.verify(token);
        Map map = new HashMap();
        jwt.getClaims().forEach(map::put);
        return map;

    }*/
}
