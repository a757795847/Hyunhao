package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;

import java.io.IOException;
import java.util.Map;

/**
 * Created by admin5 on 17/1/21.
 */
public class AuthenticationService implements IAuthenticationService {
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
            map.get("authorization_info");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
