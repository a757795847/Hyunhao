package com.zy.gcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin5 on 17/2/27.
 */
@Controller
@RequestMapping("proxy")
public class  ProxyController{
/*    @RequestMapping
    public  String home(){
        return null;
    }*/
    @RequestMapping
    public @ResponseBody Object list(){
        return null;
    }

}
