package com.zy.gcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.ICodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static com.zy.gcode.controller.delegate.ControllerStatus.error;
import static com.zy.gcode.controller.delegate.ControllerStatus.ok;

/**
 * Created by admin5 on 17/1/17.
 */
@Controller
@RequestMapping("code")
public class CodeController {
    @Autowired
    ICodeService iCodeService;

    @RequestMapping("userinfo")
    public String callback(String code, String state, String appid) {
        if (StringUtils.isEmpty(code)) {
            return "redirect:error.html?message=code is empty";
        }
        System.out.println("code:" + code);
        System.out.println("state:" + state);
        CodeRe<String> codeRe = iCodeService.token(code, state, appid);
        if (codeRe.isError()) {
            return "redirect:/error?message=" + codeRe.getErrorMessage();
        }

        return "redirect:" + codeRe.getMessage();
    }

    @RequestMapping("wxcode/{geappid}")
    public String wxcode(@PathVariable("geappid") String geappid,@RequestParam("redirect_url") String callback) throws UnsupportedEncodingException {
        callback = URLDecoder.decode(callback, "utf-8");
        CodeRe codeRe = iCodeService.code(geappid, callback);
        if (codeRe.isError()) {
            return "redirect:" + callback;
        }
        return "redirect:" + codeRe.getMessage();
    }

    @RequestMapping("getoken/{code}")
    public
    @ResponseBody
    Map wxtoken(@PathVariable String code) {
        CodeRe codeRe = iCodeService.geToken(code);
        if (codeRe.isError()) {
            return error(codeRe.getErrorMessage());
        }
        Map map = new HashMap(4);
        map.put("access_token", codeRe.getMessage());
        map.put("expirs", 7200);
        return ok(map);
    }

    @RequestMapping("guserinfo")
    public
    @ResponseBody
    Map guserinfo(String token) {
        CodeRe<User> codeRe = iCodeService.getUser(token);
        if (codeRe.isError()) {
            return error(codeRe.getErrorMessage());
        }
        Map map = new HashMap(4);
        try {
            map.put("userinfo", CodeService.objectMapper.writeValueAsString(codeRe.getMessage()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ok(map);
    }

    @RequestMapping("userinfoTest")
    public
    @ResponseBody
    String userinfoTest(@RequestBody String str) {
        return str;
    }

}
