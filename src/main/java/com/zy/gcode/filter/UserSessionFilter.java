package com.zy.gcode.filter;

import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by admin5 on 17/2/20.
 */
public class UserSessionFilter extends PassThruAuthenticationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
       HttpServletRequest request1  =  (HttpServletRequest)request;
        return super.isAccessAllowed(request, response, mappedValue)&&request1.getSession(true).getAttribute("operator")!=null;
    }
}
