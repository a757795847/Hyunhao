package com.zy.gcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin5 on 17/2/16.
 */
@Controller
public class IndexController {

    @RequestMapping("/login")
    public String login() {

        return "/views/proxy/login.html";
    }

    @RequestMapping("/unAuthorization")
    public
    @ResponseBody
    String unAuthorization() {
        return "您无权访问该页面";
    }

    @RequestMapping("/")
    public String index() {
        return "/views/publicNumber/goodcomment.html";
    }
}
