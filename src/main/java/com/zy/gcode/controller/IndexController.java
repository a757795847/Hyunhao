package com.zy.gcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by admin5 on 17/2/16.
 */
@Controller
public class IndexController {

    @RequestMapping("login")
    public String login(){

        return "/views/proxy/login.html";
    }
}
