package com.zy.gcode.filter;

import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.utils.JsonUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by admin5 on 17/3/22.
 */
public class JwtAuthenticationFilter extends AuthenticatingFilter {
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        Map<String, String> map = JsonUtils.asObj(Map.class, request.getInputStream());
        String username = map.get("username");
        String password = map.get("password");
        return new UsernamePasswordToken(username, password);
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        if (e instanceof UnknownAccountException) {
            try {
                response.getWriter().write(JsonUtils.objAsString(ControllerStatus.error("用户名不存在")));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (e instanceof IncorrectCredentialsException) {
            try {
                response.getWriter().write(JsonUtils.objAsString(ControllerStatus.error("密码不正确")));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


        return false;
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        if (savedRequest != null && savedRequest.getMethod().equals(AccessControlFilter.GET_METHOD)) {
            response.getWriter().println("bearer " + JsonUtils.objAsString(ControllerStatus.ok()));
            return false;
        }

        response.getWriter().println(JsonUtils.objAsString(ControllerStatus.ok()));
        //we handled the success redirect directly, prevent the chain from continuing:
        return false;
    }

    protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
        return (request instanceof HttpServletRequest) && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
    }

}
