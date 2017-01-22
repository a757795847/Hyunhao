package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.ICodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
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
    public String callback(HttpServletRequest request){
        System.out.println(request.getParameterMap());
       /* if(StringUtils.isEmpty(code)){
            return "redirect:error.html?message=code is empty";
        }
        System.out.println("code:"+code);
        System.out.println("state:"+state);
       CodeRe<String> codeRe =  iCodeService.token(code,state,appid);
       if(codeRe.isError()){
           return "redirect:error.html?message="+codeRe.getErrorMessage();
       }
*/
        //return "redirect:"+codeRe.getMessage();
        return "redirect:111";
    }

    @RequestMapping("wxcode/{geappid}/{callback}")
    public String wxcode(@PathVariable("geappid") String geappid,@PathVariable("callback") String callback) throws UnsupportedEncodingException{

      callback = URLDecoder.decode(callback,"utf-8");
       CodeRe codeRe = iCodeService.code(geappid,callback);
       if(codeRe.isError()){
           return "redirect:"+callback;
       }
       return "redirect:"+codeRe.getMessage();
    }

    @RequestMapping("wxtoken/{code}")
    public @ResponseBody Map wxtoken(@PathVariable String code){
        CodeRe codeRe = iCodeService.geToken(code);
        if(codeRe.isError()){
                return error(codeRe.getErrorMessage());
        }
        Map map = new HashMap(4);
        map.put("access_token",codeRe.getMessage());
        map.put("expirs",7200);
       return  ok(map);
    }
    @RequestMapping("guserinfo")
    public @ResponseBody
    Map guserinfo(String token){
       CodeRe<User> codeRe= iCodeService.getUser(token);
        if(codeRe.isError()){
            return error(codeRe.getErrorMessage());
        }
        Map map = new HashMap(4);
        map.put("userinfo",codeRe.getMessage());
        return ok(map);
    }

}
