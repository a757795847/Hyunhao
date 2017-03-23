package com.zy.gcode.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zy.gcode.utils.Du;
import com.zy.gcode.utils.JsonUtils;
import com.zy.gcode.utils.JwtUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by admin5 on 17/3/23.
 */
public class JwtHttpAuthenticationFilter extends BasicHttpAuthenticationFilter {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            JwtUtils.setResponse(servletResponse,(String)token.getPrincipal());
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        super.onAccessDenied(request,response);
        return false;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        Map<String, String> map;
        try {
            ServletInputStream inputStream = request.getInputStream();
            if (inputStream.isFinished()) {
                Du.dPl("错误!request content为空");
                return null;
            }

            map = JsonUtils.asObj(Map.class, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (map != null && map.containsKey(USERNAME)) {
            return new UsernamePasswordToken(map.get(USERNAME), map.get(PASSWORD) == null ? "" : map.get(PASSWORD));
        }

        return new UsernamePasswordToken("", "");

    }


    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return pathsMatch(getLoginUrl(), request) &&
                ((request instanceof HttpServletRequest) && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD));
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse servletResponse = WebUtils.toHttp(response);
        servletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        servletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST");
        servletResponse.setHeader("Access-Control-Allow-Origin", "*");
        servletResponse.setHeader("Allow", "GET,POST");
        if (((request instanceof HttpServletRequest) && WebUtils.toHttp(request).getMethod().equalsIgnoreCase("OPTIONS"))) {
            servletResponse.setStatus(200);
            return false;
        }

        return super.preHandle(request, response);
    }

}
