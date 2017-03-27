package com.zy.gcode.filter;

import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin5 on 17/3/27.
 */
public class AcrossOrigAnonymousFilter extends AnonymousFilter  {
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
