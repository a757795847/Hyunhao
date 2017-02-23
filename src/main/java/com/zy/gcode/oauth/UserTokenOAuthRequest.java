package com.zy.gcode.oauth;

/**
 * Created by admin5 on 17/2/23.
 */
public class UserTokenOAuthRequest extends AbstractOAuthRequest {

    public static final String APPID = "appid";
    public static final String GRANT_TYPE = "grant_type";
    public static final String COMPONENT_APPID = "component_appid";
    public static final String COMPONENT_ACCESS_TOKEN = "component_access_token";


    public UserTokenOAuthRequest(){
        super("https://api.weixin.qq.com/sns/oauth2/component/access_token");
    }

    @Override
    public Object build() {
        return null;
    }
}
