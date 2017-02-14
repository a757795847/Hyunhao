package com.zy.gcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.IAuthenticationService;
import com.zy.gcode.service.IWechatService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin5 on 17/2/14.
 */
@RequestMapping("view/wechat")
@Controller
public class WechatController {
    @Autowired
    IAuthenticationService authenticationService;

    @Autowired
    IWechatService wechatService;

    @RequestMapping("home")
    public String home(HttpServletRequest request){
       HttpSession session = request.getSession(true);
      User user  = (User) session.getAttribute("c_user");

       if(user!=null||true) {
           Map<String, String> map = new HashMap<>();

           map.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
           map.put("nonceStr", UniqueStringGenerator.getUniqueCode());
           map.put("appid", "wxa8febcce6444f95f");
           map.put("signature", signature(map, request.getRequestURL().toString()));


           try {
               request.setAttribute("jsonConfig", Constants.objectMapper.writeValueAsString(map));
           } catch (JsonProcessingException e) {
               e.printStackTrace();
           }


           return "/views/wechat/submit.html";
       }
        try {
            return  "redirect:http://open.izhuiyou.com/access/wxcode/ge111"+"?redirect_url="+ URLEncoder.encode("http://open.izhuiyou.com/middle/token","utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("submit")
    public String submit(@RequestParam(required = false) String image1,@RequestParam(required = false) String image2,
                         @RequestParam(required = false) String image3,@RequestParam String billno,HttpSession session) throws IOException{
            User user =  (User)session.getAttribute("c_user");
            if(user ==null){
                return "登录以过期,请刷新";
            }
          CodeRe<String> codeRe =  wechatService.sumbit(image1,image2,image3,billno,user.getOpenId());
            if(codeRe.isError()){
                return codeRe.getErrorMessage();
            }
            return codeRe.getMessage();
    }

    private String signature(Map<String,String> map,String url){
        Map<String,String> signatureMap = new HashMap();
        signatureMap.put("timestamp",map.get("timestamp"));
        signatureMap.put("noncestr",map.get("nonceStr"));
        CodeRe<TokenConfig> configCodeRe = authenticationService.getJsapiTicket();
        if(configCodeRe.isError()){
            throw  new IllegalArgumentException();
        }
        signatureMap.put("jsapi_ticket",configCodeRe.getMessage().getToken());
        signatureMap.put("url",url);
        Set<String> keys = signatureMap.keySet();
        Object[] objs =  keys.toArray();
        Arrays.sort(objs);
        StringBuilder builder = new StringBuilder();
        for(int i = 0 ;i <objs.length;i++){
            builder.append(objs[i].toString()).append("=")
                    .append(signatureMap.get(objs[i])).append("&");
        }

      return UniqueStringGenerator.SHA1(builder.substring(0,builder.length()-1));

    }

    @RequestMapping("test")
    public  String test(HttpServletRequest request){
        request.setAttribute("user","112334");
        return "/views/wechat/submit.html";
    }

}
