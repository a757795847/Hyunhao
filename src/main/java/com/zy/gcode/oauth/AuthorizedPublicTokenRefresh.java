package com.zy.gcode.oauth;

import com.zy.gcode.utils.HttpClientUtils;

import java.util.Map;

/**
 * Created by admin5 on 17/3/10.
 */
public class AuthorizedPublicTokenRefresh extends  AbstractOAuthRequest<Map> {
    public static final String  PRE_COMPONENT_ACCESS_TOKEN ="component_access_token";
    public static final String  BAY_COMPONENT_APPID ="component_appid";
    public static final String  BAY_AUTHORIZER_APPID ="authorizer_appid";
    public static final String  BAY_AUTHORIZER_REFRESH_TOKEN ="authorizer_refresh_token";


    public AuthorizedPublicTokenRefresh(){
        super("https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token");
    }

    @Override
    public Map start() {
        return HttpClientUtils.mapPostSend(buildParams(),buildBody());
    }

}
