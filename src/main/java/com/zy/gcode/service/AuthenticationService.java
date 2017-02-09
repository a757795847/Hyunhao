package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.AppInterface;
import com.zy.gcode.pojo.AuthorizationInfo;
import com.zy.gcode.pojo.ComponetToken;
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
    public CodeRe<ComponetToken> componetToekn() {
        String componetAppid = Constants.properties.getProperty("platform.appid");
      ComponetToken componetToken =  persistenceService.get(ComponetToken.class,componetAppid);
        try {
            componetToken.getExpiresIn();
        } catch (NullPointerException e) {
            componetToken = new ComponetToken();
            componetToken.setComponetAppid(componetAppid);
        }

        if(componetToken.getUpdateTime()==null||DateUtils.isOutOfDate(componetToken.getUpdateTime(),componetToken.getExpiresIn())){
            String ticket = Constants.properties.getProperty("componentVerifyTicket");
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
               componetToken.setComponentAccessToken(map.get("component_access_token").toString());
               componetToken.setExpiresIn(Long.parseLong(map.get("expires_in").toString()));
            persistenceService.updateOrSave(componetToken);

            }

        return CodeRe.correct(componetToken);
    }




    @Override
    public String decrpt(String msg_signature, String timestamp, String nonce, String str) {
        try(OutputStream outputStream =new FileOutputStream(new ClassPathResource("config.properties").getFile())) {
            Constants.properties.setProperty("componentVerifyTicket",WxXmlParser.elementString(wxBizMsgCrypt.decryptMsg(msg_signature,timestamp,nonce,str),"ComponentVerifyTicket"));
            Constants.properties.store(outputStream,"时间:"+DateUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss"));
            outputStream.flush();
            System.out.println("ComponentVerifyTicket已更新");
        } catch (AesException e) {
            e.printStackTrace();
        }catch (IOException e){
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
}
