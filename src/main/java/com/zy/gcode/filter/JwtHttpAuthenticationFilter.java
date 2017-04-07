package com.zy.gcode.filter;

import com.zy.gcode.utils.Du;
import com.zy.gcode.utils.JsonUtils;
import com.zy.gcode.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
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
            JwtUtils.setResponse(servletResponse, (String) token.getPrincipal());
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        super.onAccessDenied(request, response);
        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //因为前段在登录时有可能传入jwt
     return super.isAccessAllowed(request, response, mappedValue)&&(!isLoginRequest(request, response));


    }

    protected boolean isRefreshRequest(HttpServletRequest request){
        return  WebUtils.getPathWithinApplication(request).equals("/auth/refresh");
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        Map<String, String> map;
        try {
            ServletInputStream inputStream = request.getInputStream();
            if (inputStream.isFinished()||inputStream.available()==0) {
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
        Du.dPl("requestUrl:" + WebUtils.toHttp(request).getRequestURI());
        return pathsMatch(getLoginUrl(), request) &&
                ((request instanceof HttpServletRequest) && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD));
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse servletResponse = WebUtils.toHttp(response);
        JwtUtils.setAcrossOrigin(servletResponse);
        if (((request instanceof HttpServletRequest) && WebUtils.toHttp(request).getMethod().equalsIgnoreCase("OPTIONS"))) {
            servletResponse.setStatus(200);
            return false;
        }
        boolean flag = super.preHandle(request, response);
        if(flag){
            pre1PostHandle(request,response);
        }
        return flag;
    }

    protected void pre1PostHandle(ServletRequest request, ServletResponse response) throws Exception {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        if(username!=null){
            JwtUtils.setResponseWithNoExpires(WebUtils.toHttp(response),(String) SecurityUtils.getSubject().getPrincipal());
        }
    }
}
