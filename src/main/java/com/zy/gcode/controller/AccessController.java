package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.GeToken;
import com.zy.gcode.pojo.WechatUserInfo;
import com.zy.gcode.service.intef.ICodeService;
import com.zy.gcode.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        CodeRe<String> codeRe = iCodeService.token(code, state, appid);
        if (codeRe.isError()) {
            return "redirect:/index.jsp?message=" + codeRe.getErrorMessage();
        }

        return "redirect:" + codeRe.getMessage();
    }

    @RequestMapping("wxcode/{tAppid}")
    public String wxcode(@PathVariable("tAppid") String tAppid, @RequestParam("redirect_url") String callback, String state) throws UnsupportedEncodingException {
        callback = URLDecoder.decode(callback, "utf-8");
        CodeRe codeRe = iCodeService.code(tAppid, callback, state);
        if (codeRe.isError()) {
            return "redirect:" + callback;
        }
        return "redirect:" + codeRe.getMessage();
    }

    @RequestMapping("getoken/{code}/{tappid}")
    public
    @ResponseBody
    Map wxtoken(@PathVariable String code, @PathVariable String tappid) {
        CodeRe<GeToken> codeRe = iCodeService.geToken(code, tappid);
        if (codeRe.isError()) {
            return error(codeRe.getErrorMessage());
        }
        Map map = new HashMap(4);
        map.put("access_token", codeRe.getMessage().getGeTokenM());
        map.put("openid", codeRe.getMessage().getOpenid());
        map.put("expirs", 7000);
        return ok(map);
    }

    @RequestMapping("guserinfo")
    public
    @ResponseBody
    Map guserinfo(String token, String zyid) {
        CodeRe<WechatUserInfo> codeRe = iCodeService.getUser(zyid, token);
        if (codeRe.isError()) {
            return error(codeRe.getErrorMessage());
        }
        Map map = new HashMap(4);
        map.put("userinfo", JsonUtils.objAsString(codeRe.getMessage()));
        return ok(map);
    }


}
