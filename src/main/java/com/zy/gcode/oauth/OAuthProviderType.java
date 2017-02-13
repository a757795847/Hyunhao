package com.zy.gcode.oauth;

/**
 * Created by admin5 on 17/2/10.
 */
public enum OAuthProviderType {
    WEIXIN("https://open.weixin.qq.com/connect/oauth2/authorize",
            "https://api.weixin.qq.com/sns/oauth2/component/access_token",
            "微信");

    private String code_url;
    private String token_url;
    private String name;

    OAuthProviderType(String code_url, String token_url, String name) {
        this.code_url = code_url;
        this.token_url = token_url;
        this.name = name;
    }

    public String getCode_url() {
        return code_url;
    }

    public String getToken_url() {
        return token_url;
    }

    public String getName() {
        return name;
    }
}
