package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.ControllerStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin5 on 17/2/15.
 */
@RequestMapping("operator")
@Controller
public class OperatorController {

    @RequestMapping("login")
    public @ResponseBody
    Map login(@RequestBody Map map){
        String username = (String)map.get("username");
        String password = (String)map.get("password");
      Subject subject = SecurityUtils.getSubject();
      UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
        try {
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
           return ControllerStatus.error("用户名不存在");
        }catch (IncorrectCredentialsException e){
            subject.getSession().removeAttribute("operator_name");
            return ControllerStatus.error("密码不正确");
        }
        Map result = new HashMap(2);
        result.put("url","/order/home");
        return ControllerStatus.ok(result);
    }

    @RequestMapping("register")
    public String register(String nick,String password,String username){
        return  null;
    }


}
