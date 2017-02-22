package com.zy.gcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.IAuthenticationService;
import com.zy.gcode.service.IWechatService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.wx.JsapiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("home")
    public String home(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("c_user");

        if (user != null) {
            Map<String, String> map = JsapiUtils.sign(authenticationService.getJsapiTicketByAppid("wx653d39223641bea7").getMessage().getToken(),
                    request.getRequestURL().toString());
            map.put("appid", "wx653d39223641bea7");

            try {
                request.setAttribute("jsonConfig", Constants.objectMapper.writeValueAsString(map));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }


            return "/views/wechat/submit.html";
        }
        try {
            return "redirect:http://open.izhuiyou.com/access/wxcode/ge111" + "?redirect_url=" + URLEncoder.encode("http://open.izhuiyou.com/middle/token", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("submit")
    @ResponseBody
    public Map submit(@RequestParam(required = false) String image1, @RequestParam(required = false) String image2,
                      @RequestParam(required = false) String image3, @RequestParam String billno, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("c_user");
        if (user == null) {
            return ControllerStatus.error("登录过期");
        }
        CodeRe<String> codeRe = wechatService.sumbit(image1, image2, image3, billno, user.getOpenId(), user.getAppid());
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage());
    }


}
