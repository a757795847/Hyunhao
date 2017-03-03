package com.zy.gcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin5 on 17/1/23.
 */
@Controller
public class ExceptionController {
    @RequestMapping("error")
    public
    @ResponseBody
    String error(String message) {
        return message;
    }
}
