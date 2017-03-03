package com.zy.gcode.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by admin5 on 17/2/23.
 */
public class UserTokenOAuthRequest extends AbstractOAuthRequest<UserTokenOAuthRequest.AccessToken> {

    public static final String APPID = "appid";
    public static final String CODE = "code";
    public static final String GRANT_TYPE = "grant_type";
    public static final String COMPONENT_APPID = "component_appid";
    public static final String COMPONENT_ACCESS_TOKEN = "component_access_token";


    public UserTokenOAuthRequest() {
        super("https://api.weixin.qq.com/sns/oauth2/component/access_token");
    }

    @Override
    public AccessToken start() {
        return getObj(AccessToken.class);
    }

/*    public static void main(String[] args) throws Exception{
      System.out.println(  Constants.objectMapper.readValue("{\n" +
                "\"access_token\":\"ACCESS_TOKEN\",\n" +
                "\"expires_in\":7200,\n" +
                "\"refresh_token\":\"REFRESH_TOKEN\",\n" +
                "\"openid\":\"OPENID\",\n" +
                "\"scope\":\"SCOPE\"\n" +
                ",\"errcode\":1}",AccessToken.class)
        );}*/

    public static class AccessToken {
        /* "access_token":"ACCESS_TOKEN",
                 "expires_in":7200,
                 "refresh_token":"REFRESH_TOKEN",
                 "openid":"OPENID",
                 "scope":"SCOPE"
                 {"errcode":40029,"errmsg":"invalid code"}*/
        public AccessToken() {

        }

        private String accessToken;
        private Integer expiresIn;
        private String refreshToken;
        private String openid;
        private String scope;
        private Integer errcode;
        private String errmsg;
        @JsonIgnore
        private boolean error;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public Integer getErrcode() {
            return errcode;
        }

        public void setErrcode(Integer errcode) {
            this.errcode = errcode;
            setError(true);
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            setError(true);
            this.errmsg = errmsg;
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return "AccessToken{" +
                    "accessToken='" + accessToken + '\'' +
                    ", expiresIn=" + expiresIn +
                    ", refreshToken='" + refreshToken + '\'' +
                    ", openid='" + openid + '\'' +
                    ", scope='" + scope + '\'' +
                    ", errcode=" + errcode +
                    ", errmsg='" + errmsg + '\'' +
                    ", error=" + error +
                    '}';
        }
    }
}
