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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

       if(user!=null) {
           Map<String, String> map = sign(authenticationService.getJsapiTicket().getMessage().getToken(),request.getRequestURL().toString());

           map.put("appid", "wx653d39223641bea7");



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

/*    private String signature(Map<String,String> map,String url){
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

    }*/

    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        //ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }



    @RequestMapping("test")
    public  String test(HttpServletRequest request){
        request.setAttribute("user","112334");
        return "/views/wechat/submit.html";
    }

}
