package com.zy.gcode.service;

import com.zy.gcode.controller.AuthenticationController;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.AuthorizationInfo;
import com.zy.gcode.pojo.ComponetToken;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.wx.AesException;
import com.zy.gcode.utils.wx.WXBizMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/1/21.
 */
@Component
public class AuthenticationService implements IAuthenticationService {
    public  static String ComponentVerifyTicket ="ticket@@@JFNZ5BJxbfCveZ4XoUw-SUxYNC7B1ZasHWXI9nVKSrCBWsXozPamwHOklKWYyOrtyj9jN0_ORL9Z7lOWBpXH3g";

    @Autowired
    PersistenceService persistenceService;

    @Override
    public CodeRe saveServerToken(String content) {
/*        {"authorization_info":{"authorizer_appid":"wx653d39223641bea7",
                "authorizer_access_token":"nH9FksTs4eWAM4EGJrTfTMsSGyJfuzN_5atu4aQpx0rbF624OWrtDYlNpeaf78E7pa9EIlDFHpO2VksaOW1Gfs9iFbqYFOHAd2eHLPOib0IpAGVswZ_X1Sh4bQcztjM2YCGfAGDUYC",
                "expires_in":7200,
                "authorizer_refresh_token":"refreshtoken@@@abWK9VW_xjLWXecdIcWw8fyfA-iUIen-reaDPjL6r3E",
                "func_info":[{"funcscope_category":{"id":1}},
            {"funcscope_category":{"id":15}},{"funcscope_category":{"id":4}},
            {"funcscope_category":{"id":2}},{"funcscope_category":{"id":9}}]}}*/
        try {
            Map<String,Map> map= CodeService.objectMapper.readValue(content, Map.class);
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
            persistenceService.updateOrSave(authorizationInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CodeRe<ComponetToken> componetToekn() {
      ComponetToken componetToken =  persistenceService.get(ComponetToken.class,"wxa8febcce6444f95f");

        try {
            componetToken.getExpiresIn();
        } catch (NullPointerException e) {
            componetToken = new ComponetToken();
            componetToken.setComponetAppid("wxa8febcce6444f95f");
        }

        if(componetToken.getUpdateTime()==null||DateUtils.isOutOfDate(componetToken.getUpdateTime(),componetToken.getExpiresIn())){
               if(ComponentVerifyTicket==null){
                    return CodeRe.error("ComponentVerifyTicket is null");
               }
                Map map = HttpClientUtils.mapSSLPostSend("https://api.weixin.qq.com/cgi-bin/component/api_component_token"
                        ,"{ \"component_appid\":\"wxa8febcce6444f95f\" ," +
                                "\"component_appsecret\": \"5299dc17f84a708b995c85d6587e5b02\", " +
                                "\"component_verify_ticket\":\""+ComponentVerifyTicket +
                                "\"}");
               componetToken.setComponentAccessToken(map.get("component_access_token").toString());
               componetToken.setExpiresIn(Long.parseLong(map.get("component_access_token").toString()));

            }
        persistenceService.updateOrSave(componetToken);
        return CodeRe.correct(componetToken);
    }

    @Override
    public String decrpt(String msg_signature, String timestamp, String nonce, String str) {
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt("ji0a4n2s6o7n","S767sdf5sdHUfy8Sj7edR86wsEr6dh5giYfu6Tr7g8h","wxa8febcce6444f95f");
            ComponentVerifyTicket= WxXmlParser.elementString(wxBizMsgCrypt.decryptMsg(msg_signature,timestamp,nonce,str),"ComponentVerifyTicket");
            System.out.println(ComponentVerifyTicket);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return null;
    }
}
