package com.zy.gcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.service.IAuthenticationService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("submit")
    public String submit(HttpServletRequest request){
        Map<String,String> map = new HashMap<>();

        map.put("timestamp",String.valueOf(System.currentTimeMillis()));
        map.put("nonceStr", UniqueStringGenerator.getUniqueCode());
        map.put("appid","wx653d39223641bea7");
        map.put("signature",signature(map,request.getRequestURL().toString()));


        try {
            request.setAttribute("jsonConfig", Constants.objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return "/views/wechat/submit.html";
    }

    private String signature(Map<String,String> map,String url){
        Map<String,String> signatureMap = new HashMap();
        signatureMap.put("timestamp",map.get("timestamp"));
        signatureMap.put("nonceStr",map.get("noncestr"));
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
      return   builder.substring(0,builder.length()-1);

    }

    @RequestMapping("test")
    public  String test(HttpServletRequest request){
        request.setAttribute("user","112334");
        return "/views/wechat/submit.html";
    }

}
