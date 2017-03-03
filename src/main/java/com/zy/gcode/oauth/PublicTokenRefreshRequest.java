package com.zy.gcode.oauth;

import com.zy.gcode.utils.HttpClientUtils;
import org.apache.http.HttpResponse;

/**
 * Created by admin5 on 17/2/24.
 * 接口调用请求说明
 * http请求方式: POST（请使用https协议）
 * https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=xxxxx
 * POST数据示例:
 * {
 * "component_appid":"appid_value",
 * "authorizer_appid":"auth_appid_value",
 * "authorizer_refresh_token":"refresh_token_value",
 * }
 * 返回结果示例
 * {
 * "authorizer_access_token": "aaUl5s6kAByLwgV0BhXNuIFFUqfrR8vTATsoSHukcIGqJgrc4KmMJ-JlKoC_-NKCLBvuU1cWPv4vDcLN8Z0pn5I45mpATruU0b51hzeT1f8",
 * "expires_in": 7200,
 * "authorizer_refresh_token": "BstnRqgTJBXb9N2aJq6L5hzfJwP406tpfahQeLNxX0w"
 * }
 */
public class PublicTokenRefreshRequest extends AbstractOAuthRequest {

    public final static String PRA_COMPONENT_APPID = "component_appid";
    public final static String PRA_AUTHORIZER_APPID = "authorizer_appid";
    public final static String PRA_AUTHORIZER_REFRESH_TOKEN = "component_appid";


    public PublicTokenRefreshRequest() {
        super("https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token");
    }

    @Override
    public Object start() {
        HttpResponse response = HttpClientUtils.postSend(buildParams(), buildBody());
        HttpClientUtils.checkRespons(response);
        return null;
    }

    public static class TokenInfo {
        private String authorizerAccessToken;
        private Integer expiresIn;
        private String authorizerRefreshToken;
    }
}
