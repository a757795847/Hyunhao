package com.zy.gcode.controller;


import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.service.IAuthenticationService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by admin5 on 17/1/18.
 */
@Controller
public class AuthenticationController {

    @Autowired
    IAuthenticationService authenticationService;


    @RequestMapping("auth/receive")
    public @ResponseBody String getGeCode(String msg_signature,String timestamp,String nonce,@RequestBody(required = false) String str){
        authenticationService.decrpt(msg_signature, timestamp, nonce, str);
        return "success";
    }

    @RequestMapping("auth/index")
    public String authAppid(){
        CodeRe<TokenConfig> codeRe = authenticationService.componetToekn();
        if(codeRe.isError()){
            return "error.jsp?message="+codeRe.getErrorMessage();
        }
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+codeRe.getMessage().getToken();
        Map<String,String> map=  HttpClientUtils.mapPostSend(url,"{\"component_appid\":\"wxa8febcce6444f95f\"}");
       return "redirect:https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="+Constants.properties.getProperty("platform.appid")+
               "&pre_auth_code="+map.get("pre_auth_code")+"&redirect_uri=http://open.izhuiyou.com/auth/code";

    }

    @RequestMapping("auth/code")
    public @ResponseBody  String serverCode(String auth_code,String expires_in){
        CodeRe<TokenConfig> componetTokenCodeRe = authenticationService.componetToekn();
        if(componetTokenCodeRe.isError()){
            return  componetTokenCodeRe.getErrorMessage();
        }

      HttpResponse response = HttpClientUtils.postSend("https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="+componetTokenCodeRe.getMessage().getToken(),
                "{\"component_appid\":\""+ Constants.properties.getProperty("platform.appid")+"\" ,\"authorization_code\": \""+auth_code+"\"}");
      StringBuilder content = new StringBuilder();
      try(BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"))){
            String line;
            while((line = reader.readLine())!=null){
                content.append(line);
            }
      }catch (Exception e){
            e.printStackTrace();
      }

     CodeRe codeRe =  authenticationService.saveServerToken(content.toString(),componetTokenCodeRe.getMessage().getToken());
      if(codeRe.isError()){
          return  codeRe.getErrorMessage();
      }
      return "authorization success";

    }

    @RequestMapping("index")
    public @ResponseBody String index(){
     return  "welcome!";
    }


    }
