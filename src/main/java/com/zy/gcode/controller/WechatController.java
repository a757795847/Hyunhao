package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.pojo.WechatUserInfo;
import com.zy.gcode.service.IAuthenticationService;
import com.zy.gcode.service.IWechatService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.JsonUtils;
import com.zy.gcode.utils.wx.JsapiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by admin5 on 17/2/14.
 */
@RequestMapping("view/wechat")
@Controller
public class WechatController {
    @Autowired
    IAuthenticationService authenticationService;

    @Autowired
    IWechatService wechatService;

    @RequestMapping("home/{tAppid}")
    public String home(@PathVariable("tAppid") String tAppid,HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        WechatUserInfo wechatUserInfo = (WechatUserInfo) session.getAttribute("c_user");
        if(wechatUserInfo ==null){
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if(Constants.debug){
                    System.out.println("cookie:"+cookie);
                }
                if (cookie.getName().equals("user_openid")){
                    wechatUserInfo = wechatService.getUser(cookie.getValue());
                    session.setAttribute("c_user",wechatUserInfo);
                }
            }
        }
        WechatPublicServer wechatPublicServer = wechatService.getWechatPublic(tAppid);

        if (wechatUserInfo != null) {
            Map<String, String> map = JsapiUtils.sign(authenticationService.getJsapiTicketByAppid(wechatPublicServer.getWxAppid()).getMessage().getToken(),
                    request.getRequestURL().toString());
            map.put("appid", wechatPublicServer.getWxAppid());
            request.setAttribute("jsonConfig", JsonUtils.objAsString(map));
            return "/views/wechat/submit.html";
        }
        try {
            return "redirect:http://open.izhuiyou.com/access/wxcode/" + tAppid + "?redirect_url="
                    + URLEncoder.encode("http://open.izhuiyou.com/middle/token", "utf-8") + "&state=" + URLEncoder.encode(request.getRequestURL().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("submit")
    @ResponseBody
    public Map submit(@RequestParam(required = false) String image1, @RequestParam(required = false) String image2,
                      @RequestParam(required = false) String image3, @RequestParam String billno, HttpSession session) throws IOException {
        WechatUserInfo wechatUserInfo = (WechatUserInfo) session.getAttribute("c_user");
        if (wechatUserInfo == null) {
            return ControllerStatus.error("登录过期");
        }
        CodeRe<String> codeRe = wechatService.sumbit(image1, image2,image3,billno,wechatUserInfo.getOpenId(),wechatUserInfo.getAppid());
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage());
    }


}
