package com.zy.gcode.controller;

import com.zy.gcode.cache.OperatorCache;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.intef.IOperatorService;
import com.zy.gcode.service.intef.IUserService;
import com.zy.gcode.utils.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by admin5 on 17/2/15.
 */
@Controller
public class OperatorController {

    Map<String,Map> mapCache = new ConcurrentHashMap<>();




    @Autowired
    OperatorCache operatorCache;

    @Autowired
    IOperatorService operatorService;

    @Autowired
    IUserService userService;


    @RequestMapping("/auth/user")
    public @ResponseBody Object user(){
       String userName = (String)SecurityUtils.getSubject().getPrincipal();
      return ControllerStatus.ok(operatorService.get(userName));
    }

    @RequestMapping("/auth/refresh")
    public void refresh(HttpServletResponse response){
        JwtUtils.setResponse(response,(String)SecurityUtils.getSubject().getPrincipal());
    }

    @RequestMapping("/auth/logout")
    public void logout(HttpServletResponse response){
        JwtUtils.setAnonymousResponse(response);
        response.setStatus(401);
        operatorCache.put(SecurityUtils.getSubject().getPrincipal(),UniqueStringGenerator.getUniqueCode());
    }


    @RequestMapping("register")
    public
    @ResponseBody
    Object register(@RequestBody Map map) {
        VerificationInfo verificationInfo = (VerificationInfo) getSession().get("verificationInfo");
        if (verificationInfo == null) {
            return ControllerStatus.error("未验证验证码");
        }

        if (!(MzUtils.checkEntry(map, "phone") && MzUtils.checkEntry(map, "password"))) {
            return ControllerStatus.error("用户名密码不能为空");
        }
        if (!verificationInfo.verificationCode.equals(map.get("verificationCode"))) {
            return ControllerStatus.error("验证码错误");
        }

        if (verificationInfo.generationTime < (System.currentTimeMillis() - 120 * 1000)) {
            return ControllerStatus.error("验证码过期");
        }
       // getSession().remove("verificationInfo");


        CodeRe codeRe = operatorService.registerOperator(map.get("phone").toString(), map.get("password").toString());
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        mapCache.remove(SecurityUtils.getSubject().getPrincipal());
        return ControllerStatus.ok((String) codeRe.getMessage());
    }

    private Map getSession(){
       return mapCache.get(SecurityUtils.getSubject().getPrincipal());
    }


    private void sessionPut(Object key,Object value){
        Map map;
       if((map =getSession())==null){
           map = new HashMap();
           mapCache.put((String)SecurityUtils.getSubject().getPrincipal(),map);
       }
       map.put(key,value);
    }

    @RequestMapping("checkUsername/{username}")
    public
    @ResponseBody
    Object checkUsername(@PathVariable String username) {
        CodeRe codeRe = operatorService.checkUsername(username);
        if (codeRe.isError()) {
            return ControllerStatus.error();
        }
        return ControllerStatus.ok();
    }


    @RequestMapping("verificationCode")
    public
    @ResponseBody
    Object verificationCode(String phone, String code) {
        ImageInfo imageInfo = (ImageInfo) getSession().get("imageInfo");
        if (imageInfo == null) {
            return ControllerStatus.error("请先获取验证码");
        }

        if ((!imageInfo.content.equals(code)) || imageInfo.createTime < (System.currentTimeMillis() - 120 * 1000)) {
            return ControllerStatus.error("验证码错误或过期!");
        }
        VerificationInfo periousVerificationInfo = (VerificationInfo)  getSession().get("verificationInfo");
        if (periousVerificationInfo != null && periousVerificationInfo.generationTime > (System.currentTimeMillis() - 60 * 1000)) {
            return ControllerStatus.error("请60秒后再发送");
        }


        getSession().remove("imageInfo");

        CodeRe<String> codeRe = operatorService.generateVerificationCode(phone);
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        VerificationInfo verificationInfo = new VerificationInfo(codeRe.getMessage(), System.currentTimeMillis(), phone);
        sessionPut("verificationInfo", verificationInfo);
        return ControllerStatus.ok("success");
    }

    @RequestMapping("getcodeImage")
    public void getCodeImage(HttpServletResponse response) {

        CodeImage codeImage = new CodeImage();
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ImageInfo imageInfo = new ImageInfo(codeImage.getCode(), System.currentTimeMillis());
        sessionPut("imageInfo", imageInfo);
        Du.pl("获取验证码:"+imageInfo.content);
        try {
            codeImage.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/forgetHome")
    public String forgetHome() {
        return "/views/proxy/forget.html";
    }


    @RequestMapping("updatePassword")
    public
    @ResponseBody
    Object updatePassword(@RequestBody Map map) {
        VerificationInfo verificationInfo = (VerificationInfo) getSession().get("verificationInfo");
        if (verificationInfo == null) {
            return ControllerStatus.error("请先填写验证码");
        }

        if (!(MzUtils.checkEntry(map, "phone") && MzUtils.checkEntry(map, "password"))) {
            return ControllerStatus.error("用户名密码不能为空");
        }
        if (!verificationInfo.verificationCode.equals(map.get("verificationCode"))) {
            return ControllerStatus.error("验证码错误");
        }

        if (verificationInfo.generationTime < (System.currentTimeMillis() - 120 * 1000)) {
            return ControllerStatus.error("验证码过期");
        }
        getSession().remove("verificationInfo");
        CodeRe codeRe = operatorService.updatePassword(map.get("phone").toString(), map.get("password").toString());
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage().toString());
    }

    @RequestMapping("checkVerificationCode/{code}")
    public
    @ResponseBody
    Object checkVerificationCode(@PathVariable String code) {

        VerificationInfo verificationInfo = (VerificationInfo) getSession().get("verificationInfo");
        if (verificationInfo == null) {
            return ControllerStatus.error("请先填写验证码");
        }
        if (!verificationInfo.verificationCode.equals(code)) {
            return ControllerStatus.error("验证码错误");
        }

        if (verificationInfo.generationTime < (System.currentTimeMillis() - 120 * 1000)) {
            return ControllerStatus.error("验证码过期");
        }
        return ControllerStatus.ok();

    }

    @RequestMapping("innerUpdatePassword")
    public
    @ResponseBody
    Object innerUpdatePassword(@RequestBody Map map) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (!map.containsKey("oldpassword")) {
            return ControllerStatus.error("旧密码不能为空");
        }
        if (!operatorService.passwordIsTrue(map.get("oldpassword").toString(), user.getPassword())) {
            return ControllerStatus.error("旧密码错误");
        }


        if (map.get("newpassword") == null && map.get("newpassword").toString().length() == 0) {
            return ControllerStatus.error();
        }
        CodeRe codeRe = operatorService.updatePassword(user.getUsername(), map.get("newpassword").toString());

        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok();
    }


    public class VerificationInfo {
        public VerificationInfo(String verificationCode, long generationTime, String phone) {
            this.verificationCode = verificationCode;
            this.generationTime = generationTime;
            this.phone = phone;
        }

        String verificationCode;
        long generationTime;
        String phone;
    }

    public class ImageInfo {
        private String content;
        private long createTime;

        public ImageInfo(String content, long createTime) {
            this.content = content;
            this.createTime = createTime;
        }
    }

}
