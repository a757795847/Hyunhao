package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.AppInterface;
import com.zy.gcode.pojo.AuthorizationInfo;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.wx.AesException;
import com.zy.gcode.utils.wx.WXBizMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/1/21.
 */
@Component
public class AuthenticationService implements IAuthenticationService {
    @Autowired
    PersistenceService persistenceService;

    static WXBizMsgCrypt wxBizMsgCrypt = null;
    static{
        try {
            wxBizMsgCrypt = new WXBizMsgCrypt(Constants.properties.getProperty("platform.token"),
                    Constants.properties.getProperty("platform.asekey"),Constants.properties.getProperty("platform.appid"));
        } catch (AesException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CodeRe<AuthorizationInfo> saveServerToken(String content,String token) {
        try {
            Map<String,Map> map= Constants.objectMapper.readValue(content, Map.class);
            if(map.containsKey("errmsg")){
                return  CodeRe.error(map.get("errmsg").toString());
            }

            Map child = map.get("authorization_info");
            AuthorizationInfo authorizationInfo = new AuthorizationInfo();
            authorizationInfo.setAuthorizerAppid(child.get("authorizer_appid").toString());
            authorizationInfo.setAuthorizerAccessToken(child.get("authorizer_access_token").toString());
            authorizationInfo.setExpiresIn(Long.parseLong(child.get("expires_in").toString()));
            authorizationInfo.setAuthorizerRefreshToken(child.get("authorizer_refresh_token").toString());
            List<Map<String,Map>> funs = (List)child.get("func_info");
            StringBuilder builder = new StringBuilder();
            for(Map<String,Map> third:funs){
                   builder.append(third.get("funcscope_category").get("id").toString()).append(",");
            }
            authorizationInfo.setFuncInfo(builder.substring(0,builder.length()-1));
             Map map1 =  HttpClientUtils.mapSSLPostSend("https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token="+token,
                    "{" +
                            "\"component_appid\":\""+Constants.properties.getProperty("platform.appid")+"\" ," +
                            "\"authorizer_appid\": \""+authorizationInfo.getAuthorizerAppid()+"\"" +
                            "}");
             if(map1==null){
                 return CodeRe.error("获取公众号名称错误!");
             }
             authorizationInfo.setUserName((String)(map1.get("user_name")));
             authorizationInfo.setNickName((String)(map1.get("nick_name")));
             authorizationInfo.setPrincipalName((String)(map1.get("principal_name")));

            persistenceService.updateOrSave(authorizationInfo);
            return CodeRe.correct(authorizationInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CodeRe<TokenConfig> componetToekn() {
        String componetAppid = Constants.properties.getProperty("platform.appid");
        TokenConfig componetToken =  persistenceService.get(TokenConfig.class,componetAppid);
         TokenConfig componentVerifyTicket= persistenceService.get(TokenConfig.class,"componentVerifyTicket");
        try {
            componetToken.getExpiresIn();
        } catch (NullPointerException e) {
            componetToken = new TokenConfig();
            componetToken.setName(componetAppid);
        }

        if(componetToken.getUpdateTime()==null||DateUtils.isOutOfDate(componetToken.getUpdateTime(),componetToken.getExpiresIn())){
            String ticket = componentVerifyTicket.getToken();
               if(ticket==null){
                    return CodeRe.error("ComponentVerifyTicket is null");
               }
                Map map = HttpClientUtils.mapSSLPostSend("https://api.weixin.qq.com/cgi-bin/component/api_component_token"
                        ,"{ \"component_appid\":\""+componetAppid+"\" ," +
                                "\"component_appsecret\": \""+Constants.properties.getProperty("platform.secret")+"\", " +
                                "\"component_verify_ticket\":\""+ticket +
                                "\"}");
               if(map.containsKey("errmsg")){
                   return CodeRe.error((String)map.get("errmsg"));
               }
               componetToken.setToken(map.get("component_access_token").toString());
               componetToken.setExpiresIn(Long.parseLong(map.get("expires_in").toString()));
            persistenceService.updateOrSave(componetToken);

            }

        return CodeRe.correct(componetToken);
    }

    public CodeRe<TokenConfig> getJsapiTicket(){
       CodeRe<TokenConfig> componetTokenCodeRe = componetToekn();
       if(componetTokenCodeRe.isError()){
           return componetTokenCodeRe;
       }

       TokenConfig tokenConfig = persistenceService.get(TokenConfig.class,Constants.JSSDK_TICKET_NAME);
        Timestamp updateTime;
        try {
          updateTime = tokenConfig.getUpdateTime();
        } catch (NullPointerException e) {
           tokenConfig = new TokenConfig();
           tokenConfig.setName(Constants.JSSDK_TICKET_NAME);
            Map map = HttpClientUtils.mapSSLGetSend("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+componetTokenCodeRe.getMessage().getToken()+"&type=jsapi");
            if(map.containsKey("errmsg")){
                if(!map.get("errmsg").equals("ok"))
                return CodeRe.error((String)map.get("errmsg"));
            }
            tokenConfig.setToken(map.get("ticket").toString());
            persistenceService.updateOrSave(tokenConfig);
            return  CodeRe.correct(tokenConfig);
        }
        if(DateUtils.isOutOfDate(updateTime,7200)){
            Map map = HttpClientUtils.mapSSLGetSend("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+componetTokenCodeRe.getMessage().getToken()+"&type=jsapi");
            if(map.containsKey("errmsg")){
                if(!map.get("errmsg").equals("ok"))
                return CodeRe.error((String)map.get("errmsg"));
            }
            tokenConfig.setToken(map.get("ticket").toString());
            persistenceService.updateOrSave(tokenConfig);
            return  CodeRe.correct(tokenConfig);

        }

      return CodeRe.correct(tokenConfig);
    }


    @Override
    public String decrpt(String msg_signature, String timestamp, String nonce, String str) {
        TokenConfig tokenConfig = new TokenConfig();
        tokenConfig.setName("componentVerifyTicket");
        try {
            tokenConfig.setToken(WxXmlParser.elementString(wxBizMsgCrypt.decryptMsg(msg_signature,timestamp,nonce,str),"ComponentVerifyTicket"));
            persistenceService.updateOrSave(tokenConfig);
            System.out.println("componentVerifyTicket已更新");
        } catch (AesException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public AppInterface allAppInterface(String id) {
       AppInterface appInterface = persistenceService.get(AppInterface.class,id);
        try {
            appInterface.getGeAppid();
        } catch (NullPointerException e) {
           return null;
        }
        return appInterface;
    }

    @Override
    public CodeRe<TokenConfig> getJsapiTicketByAppid(String appid) {
        CodeRe<TokenConfig> wxAccessToken = getWxAccessToken(appid);
        if(wxAccessToken.isError()){
            return wxAccessToken;
        }
        String name = Constants.JSSDK_TICKET_NAME+appid;
        TokenConfig tokenConfig = persistenceService.get(TokenConfig.class,name);
        Timestamp updateTime;
        try {
            updateTime = tokenConfig.getUpdateTime();
        } catch (NullPointerException e) {
            tokenConfig = new TokenConfig();
            tokenConfig.setName(name);
            Map map = HttpClientUtils.mapSSLGetSend("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+wxAccessToken.getMessage().getToken()+"&type=jsapi");
            if(map.containsKey("errmsg")){
                if(!map.get("errmsg").equals("ok"))
                    return CodeRe.error((String)map.get("errmsg"));
            }
            tokenConfig.setToken(map.get("ticket").toString());
            tokenConfig.setExpiresIn(Long.parseLong(map.get("expires_in").toString()));
            persistenceService.updateOrSave(tokenConfig);
            return  CodeRe.correct(tokenConfig);
        }
        if(DateUtils.isOutOfDate(updateTime,tokenConfig.getExpiresIn())){
            Map map = HttpClientUtils.mapSSLGetSend("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+wxAccessToken.getMessage().getToken()+"&type=jsapi");
            if(map.containsKey("errmsg")){
                if(!map.get("errmsg").equals("ok"))
                    return CodeRe.error((String)map.get("errmsg"));
            }
            tokenConfig.setToken(map.get("ticket").toString());
            persistenceService.updateOrSave(tokenConfig);
            return  CodeRe.correct(tokenConfig);

        }

        return CodeRe.correct(tokenConfig);
    }

    @Override
    public CodeRe<TokenConfig> getWxAccessToken(String appid) {
        TokenConfig tokenConfig = persistenceService.get(TokenConfig.class,appid+"toekn");
        Timestamp updateTime;
        try {
            updateTime = tokenConfig.getUpdateTime();
        } catch (Exception e) {
            tokenConfig = new TokenConfig();
            AppInterface appInterface = persistenceService.getOneByColumn(AppInterface.class,"wxAppid",appid);
            if(appInterface==null){
                return CodeRe.error("appid 不存在");
            }
          Map map = HttpClientUtils.mapSSLGetSend("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appInterface.getSecret());
            if(map.containsKey("errmsg")){
                return CodeRe.error(map.get("errmsg").toString());
            }
            tokenConfig.setName(appid+"token");
            tokenConfig.setToken(map.get("access_token").toString());
            tokenConfig.setExpiresIn(Long.parseLong(map.get("expires_in").toString()));
            persistenceService.updateOrSave(tokenConfig);
            return CodeRe.correct(tokenConfig);
        }

        if(DateUtils.isOutOfDate(updateTime,tokenConfig.getExpiresIn())){
            AppInterface appInterface = persistenceService.getOneByColumn(AppInterface.class,"wxAppid",appid);
            if(appInterface==null){
                return CodeRe.error("appid 不存在,获取wxaccesstoken");
            }
            Map map = HttpClientUtils.mapSSLGetSend("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appInterface.getSecret());
            if(map.containsKey("errmsg")){
                return CodeRe.error(map.get("errmsg").toString());
            }
            tokenConfig.setName(appid+"token");
            tokenConfig.setToken(map.get("access_token").toString());
            tokenConfig.setExpiresIn(Long.parseLong(map.get("expires_in").toString()));
            persistenceService.updateOrSave(tokenConfig);

        }
        return CodeRe.correct(tokenConfig);

    }
}
