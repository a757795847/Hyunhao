package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.service.IOperatorService;
import com.zy.gcode.service.OperatorService;
import com.zy.gcode.utils.CodeImage;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.MzUtils;
import org.apache.http.HttpResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin5 on 17/2/15.
 */
@RequestMapping("operator")
@Controller
public class OperatorController {

    @Autowired
    IOperatorService operatorService;

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
            subject.getSession().removeAttribute("operator");
            return ControllerStatus.error("密码不正确");
        }
        Map result = new HashMap(2);
        result.put("url","/order/home");
        return ControllerStatus.ok(result);
    }

    @RequestMapping("register")
    public @ResponseBody Object register(@RequestBody Map map,HttpSession session){
       VerificationInfo verificationInfo = (VerificationInfo)session.getAttribute("verificationInfo");
       if(verificationInfo ==null){
          return ControllerStatus.error("请先填写验证码");
       }

        if(!(MzUtils.checkEntry(map,"phone")&&MzUtils.checkEntry(map,"password"))){
            return ControllerStatus.error("用户名密码不能为空");
        }
       if(!verificationInfo.phone.equals(map.get("phone"))){
           return ControllerStatus.error("验证码错误");
       }

       if(verificationInfo.generationTime<(System.currentTimeMillis()-120*1000)){
           return ControllerStatus.error("验证码过期");
       }
        session.removeAttribute("verificationInfo");


        CodeRe codeRe = operatorService.registerOperator(map.get("phone").toString(),map.get("password").toString());
       if(codeRe.isError()){
           ControllerStatus.error(codeRe.getErrorMessage());
       }
       return  ControllerStatus.ok((String)codeRe.getMessage());
    }

    @RequestMapping("verificationCode")
    public @ResponseBody Object verificationCode(String phone,String code, HttpSession session){
       ImageInfo imageInfo = (ImageInfo)session.getAttribute("imageInfo");
       if(imageInfo==null){
          return ControllerStatus.error("请输入验证码");
       }

       if((!imageInfo.content.equals(code))||imageInfo.createTime<(System.currentTimeMillis()-120*1000)){
           return ControllerStatus.error("验证码错误或过期!");
       }
        session.removeAttribute("imageInfo");

         CodeRe<String> codeRe =  operatorService.generateVerificationCode(phone);
         if(codeRe.isError()){
             return ControllerStatus.error(codeRe.getErrorMessage());
         }
         VerificationInfo verificationInfo = new VerificationInfo(codeRe.getMessage(),System.currentTimeMillis(),phone);
         session.setAttribute("verificationInfo",verificationInfo);
         return ControllerStatus.ok("success");
    }

    @RequestMapping("registerHome")
    public String registerHome(){
        return "/views/proxy/register.html";
    }

    @RequestMapping("logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }


    @RequestMapping("getcodeImage")
    public void getCodeImage(HttpServletResponse response, HttpSession session){
        CodeImage codeImage = new CodeImage();
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ImageInfo imageInfo = new ImageInfo(codeImage.getCode(),System.currentTimeMillis());
        session.setAttribute("imageInfo",imageInfo);
        try {
            codeImage.write(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private class VerificationInfo{
        public VerificationInfo(String verificationCode, long generationTime, String phone) {
            this.verificationCode = verificationCode;
            this.generationTime = generationTime;
            this.phone = phone;
        }

        String verificationCode;
        long generationTime;
        String phone;
    }
    private class  ImageInfo{
        private String content;
        private long createTime;

        public ImageInfo(String content, long createTime) {
            this.content = content;
            this.createTime = createTime;
        }
    }
}
