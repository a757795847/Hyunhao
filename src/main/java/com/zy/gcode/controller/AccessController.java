package com.zy.gcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.GeToken;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.ICodeService;
import com.zy.gcode.utils.Constants;
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
@RequestMapping("access")
public class AccessController {
    @Autowired
    ICodeService iCodeService;

    @RequestMapping("wxaccess_token")
    public String callback(String code, String state, String appid) {
        if (StringUtils.isEmpty(code)) {
            return "redirect:error.html?message=code is empty";
        }
        CodeRe<String> codeRe = iCodeService.token(code,state,appid);
        if (codeRe.isError()) {
            return "redirect:/index.jsp?message=" + codeRe.getErrorMessage();
        }

        return "redirect:" + codeRe.getMessage();
    }

    @RequestMapping("wxcode/{geappid}")
    public String wxcode(@PathVariable("geappid") String geappid,@RequestParam("redirect_url") String callback,String state) throws UnsupportedEncodingException {
        callback = URLDecoder.decode(callback, "utf-8");
        CodeRe codeRe = iCodeService.code(geappid,callback,state);
        if (codeRe.isError()) {
            return "redirect:" + callback;
        }
        return "redirect:" + codeRe.getMessage();
    }

    @RequestMapping("getoken/{code}/{geappid}")
    public
    @ResponseBody
    Map wxtoken(@PathVariable String code,@PathVariable String geappid) {
        CodeRe<GeToken> codeRe = iCodeService.geToken(code,geappid);
        if (codeRe.isError()) {
            return error(codeRe.getErrorMessage());
        }
        Map map = new HashMap(4);
        map.put("access_token",codeRe.getMessage().getGeTokenM());
        map.put("openid",codeRe.getMessage().getOpenid());
        map.put("expirs", 7000);
        return ok(map);
    }

    @RequestMapping("guserinfo")
    public
    @ResponseBody
    Map guserinfo(String token,String zyid) {
        CodeRe<User> codeRe = iCodeService.getUser(zyid,token);
        if (codeRe.isError()) {
            return error(codeRe.getErrorMessage());
        }


        Map map = new HashMap(4);
        try {
            map.put("userinfo", Constants.objectMapper.writeValueAsString(codeRe.getMessage()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ok(map);
    }


}
