package com.zy.gcode.controller;

import com.zy.gcode.pojo.WechatUserInfo;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by admin5 on 17/1/24.
 */
@Controller
@RequestMapping("middle")
public class MiddleController {
    @RequestMapping("a")
    public String a(String appid, HttpSession session) throws UnsupportedEncodingException {
        if (session.getAttribute("c_user") == null) {
            return "redirect:http://open.izhuiyou.com/code/wxcode/" + appid + "?redirect_url=" + URLEncoder.encode("http://open.izhuiyou.com/middle/token", "utf-8");
        }
        return "redirect:/html/a.html";
    }


    @RequestMapping("token")
    public String token(String code, HttpSession session, String state, String zyid) {
        Map map = HttpClientUtils.mapGetSend("http://open.izhuiyou.com/access/getoken/" + code + "/" + zyid);
        if (map == null) {
            return "redirect:/error.html";
        }
        if (map.containsKey("status") && map.get("status").equals("1")) {
            Map map1 = HttpClientUtils.mapGetSend("http://open.izhuiyou.com/access/guserinfo?token=" + map.get("access_token") + "&zyid=" + zyid);
            if (map1.containsKey("status") && map1.get("status").equals("1")) {
                String str = map1.get("userinfo").toString();
                try {
                    WechatUserInfo wechatUserInfo = Constants.objectMapper.readValue(str, WechatUserInfo.class);
                    session.setAttribute("c_user", wechatUserInfo);
                    return "redirect:" + state;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "redirect:/index.jsp";
    }
}
