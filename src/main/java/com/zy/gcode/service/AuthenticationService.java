package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.AuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/1/21.
 */
@Component
public class AuthenticationService implements IAuthenticationService {

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
}
