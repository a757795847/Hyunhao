package com.zy.gcode.filter;

import com.zy.gcode.pojo.WechatUserInfo;
import com.zy.gcode.service.WechatService;
import com.zy.gcode.utils.Constants;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by admin5 on 17/3/17.
 */
public class OpenIdFilter extends PassThruAuthenticationFilter {
    @Autowired
    WechatService wechatService;


    @Override
    protected boolean isAccessAllowed(ServletRequest request1, ServletResponse response, Object mappedValue) {

        HttpServletRequest request = (HttpServletRequest) request1;
        HttpSession session = request.getSession(true);

        WechatUserInfo wechatUserInfo = (WechatUserInfo) session.getAttribute("c_user");
        if (wechatUserInfo == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (Constants.debug) {
                        System.out.println("cookie:" + cookie.getName() + ":" + cookie.getValue());
                    }
                    if (cookie.getName().equals("user_openid")) {
                        wechatUserInfo = wechatService.getUser(cookie.getValue());
                        session.setAttribute("c_user", wechatUserInfo);
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        String uri = request.getRequestURI();
        String tAppid = uri.substring(uri.lastIndexOf("/") + 1);

        try {
            setLoginUrl("http://open.izhuiyou.com/access/wxcode/" + tAppid + "?redirect_url="
                    + URLEncoder.encode("http://open.izhuiyou.com/middle/token", "utf-8") +
                    "&state=" + URLEncoder.encode(request.getRequestURL().toString(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return false;
    }
}
