package com.zy.gcode.filter;

import com.zy.gcode.utils.Du;
import com.zy.gcode.utils.JwtUtils;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin5 on 17/3/27.
 */
public class JwtAnonymousFilter extends AnonymousFilter  {
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
             prePostHandle(request,response);
        }
        return flag;
    }

    protected void prePostHandle(ServletRequest request, ServletResponse response) throws Exception {
        if(request instanceof  HttpServletRequest){
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            String authorization = servletRequest.getHeader(JwtUtils.AUTHORIZATION);
             if(authorization!=null){
               return;
           }
            Du.dPl("anonymous:"+((HttpServletRequest) request).getRequestURI());
            JwtUtils.setAnonymousResponse(WebUtils.toHttp(response));
        }
    }
}
