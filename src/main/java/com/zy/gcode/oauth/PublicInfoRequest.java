package com.zy.gcode.oauth;

import com.zy.gcode.utils.HttpClientUtils;

import java.util.Map;

/**
 * Created by admin5 on 17/2/27.
 * 用于获取授权公众号的基本信息
 */
public class PublicInfoRequest extends AbstractOAuthRequest<Map> {

    /**
     * 服务访问token
     */
    public static final String PRA_COMPONENT_ACCESS_TOKEN = "component_access_token";
    /**
     * 服务appid
     */
    public static final String BAY_COMPONENT_APPID = "component_appid";
    /**
     * 授权方appid
     */
    public static final String BAY_AUTHORIZER_APPID = "authorizer_appid";


    public PublicInfoRequest() {
        super("https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info");
    }

    @Override
    public Map start() {
        return HttpClientUtils.mapPostSend(buildParams(), buildBody());
    }
}
