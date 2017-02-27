package com.zy.gcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by admin5 on 17/2/27.
 */
@Controller
@RequestMapping("proxy")
public class ProxyController {
    @RequestMapping
    public String home(){
        return null;
    }

}
