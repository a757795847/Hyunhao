package com.zy.gcode.oauth;

/**
 * Created by admin5 on 17/2/24.
 */
public class PublicLoginRequest extends AbstractOAuthRequest<String> {

    public static final String PRA_COMPONENT_APPID = "component_appid";
    public static final String PRA_PRE_AUTH_CODE = "pre_auth_code";
    public static final String PRA_REDIRECT_URI = "redirect_uri";

    public PublicLoginRequest() {
        super("https://mp.weixin.qq.com/cgi-bin/componentloginpage");
    }

    @Override
    public String start() {
        return buildParams();
    }
}
