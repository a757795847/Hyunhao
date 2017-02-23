package com.zy.gcode.oauth;

/**
 * Created by admin5 on 17/2/23.
 */
public class UserTokenOAuthRequest extends AbstractOAuthRequest {

    public static final String APPID = "appid";
    public static final String grant_type = "grant_type";
    public static final String component_appid = "component_appid";
    public static final String component_access_token = "component_access_token";


    public UserTokenOAuthRequest(){
        super("https://api.weixin.qq.com/sns/oauth2/component/access_token");
    }

    @Override
    public Object build() {
        return null;
    }
}
