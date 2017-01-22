package com.zy.gcode.controller;


import com.zy.gcode.service.IAuthenticationService;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.wx.AesException;
import com.zy.gcode.utils.wx.WXBizMsgCrypt;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin5 on 17/1/18.
 */
@Controller
public class AuthenticationController {
    public  static String ComponentVerifyTicket ="ticket@@@GR3DiYn44RDjBFLd-dGVjf7IzOp-n56HfNrLqAeQ5HC3GzDf3HCiLtSjbje4xDBW9Qg-3au1Dj6nbWnoujYJyQ";
    public static  Map serverToken = new HashMap();

    @Autowired
    IAuthenticationService authenticationService;

    @RequestMapping("auth/receive")
    public @ResponseBody String getGeCode(String msg_signature,String timestamp,String nonce,@RequestBody(required = false) String str){
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt("ji0a4n2s6o7n","S767sdf5sdHUfy8Sj7edR86wsEr6dh5giYfu6Tr7g8h","wxa8febcce6444f95f");
            ComponentVerifyTicket= WxXmlParser.elementString(wxBizMsgCrypt.decryptMsg(msg_signature,timestamp,nonce,str),"ComponentVerifyTicket");
        System.out.println(ComponentVerifyTicket);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping("auth/{appid}/index")
    public String authAppid(@PathVariable String appid){
        if (ComponentVerifyTicket==null){
            return "redirect:error?message=wait";
        }
        if(serverToken.containsKey("inserDate")){
            Date insertDate = (Date) serverToken.get("inserDate");
            long expirs = (long)serverToken.get("expires_in");
            if(insertDate.before(new Date(System.currentTimeMillis()-expirs*1000))){
                serverToken = HttpClientUtils.mapSSLPostSend("https://api.weixin.qq.com/cgi-bin/component/api_component_token"
                        ,"{ \"component_appid\":\"wxa8febcce6444f95f\" ," +
                                "\"component_appsecret\": \"5299dc17f84a708b995c85d6587e5b02\", " +
                                "\"component_verify_ticket\":\""+ComponentVerifyTicket +
                                "\"}");
                serverToken.put("insertDate",new Date());
            }
        }else {
            serverToken = HttpClientUtils.mapSSLPostSend("https://api.weixin.qq.com/cgi-bin/component/api_component_token"
                    ,"{ \"component_appid\":\"wxa8febcce6444f95f\" ," +
                            "\"component_appsecret\": \"5299dc17f84a708b995c85d6587e5b02\", " +
                            "\"component_verify_ticket\":\""+ComponentVerifyTicket +
                            "\"}");
            serverToken.put("insertDate",new Date());
        }

        String url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+serverToken.get("component_access_token");
        Map<String,String> map=  HttpClientUtils.mapSSLPostSend(url,"{\"component_appid\":\"wxa8febcce6444f95f\"}");
       return "redirect:https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=" +
               "wxa8febcce6444f95f&pre_auth_code="+map.get("pre_auth_code")+"&redirect_uri=http://open.izhuiyou.com/server/code";

    }

    @RequestMapping("server/code")
    public @ResponseBody  String serverCode(String auth_code,String expires_in){
      HttpResponse response = HttpClientUtils.SSLPostSend("https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="+serverToken.get("component_access_token"),
                "{\"component_appid\":\"wxa8febcce6444f95f\" ,\"authorization_code\": \""+auth_code+"\"}");
      StringBuilder content = new StringBuilder();
      try(BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"))){
            String line;
            while((line = reader.readLine())!=null){
                content.append(line);
            }
      }catch (Exception e){
            e.printStackTrace();
      }

        authenticationService.saveServerToken(content.toString());
      return "authorization success";

    }



    @RequestMapping("{appid}/receive")
    public @ResponseBody String appidRegister(@PathVariable String appid){
        System.out.println(appid);
        return "11";
    }



    @RequestMapping("index")
    public @ResponseBody String index(){
     return  "welcome!";
    }


    }
