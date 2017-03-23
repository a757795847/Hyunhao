package com.zy.gcode.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.mgt.DefaultSubjectFactory;
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
import java.io.UnsupportedEncodingException;
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
        if(webContext.isAuthenticated()){
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
        String authorization ;
        try {
            authorization = request.getHeader("authorization");
        } catch (Exception e) {
            e.printStackTrace();
            return  new WebDelegatingSubject(principals, authenticated, host, session, false,
                    request, response, securityManager);
        }
        Map map = getClaimMap(authorization);
        session.setAttributes(map);

        boolean sessionEnabled = false;
        if (map != null && map.containsKey(PublicClaims.SUBJECT)) {
            String token = ((Claim) map.get(PublicClaims.SUBJECT)).asString();
            principals = new SimplePrincipalCollection(token, ZyRealm.name);
        }
        if (principals != null) {
            authenticated = true;
        }
        return new WebDelegatingSubject(principals, authenticated, host, session, sessionEnabled,
                request, response, securityManager);
    }


    private Map getClaimMap(String token) {
        if (token == null) {
            return null;
        }
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
        return jwt.getClaims();

    }
}
