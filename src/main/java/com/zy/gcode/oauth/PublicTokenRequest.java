package com.zy.gcode.oauth;

import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.MzUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by admin5 on 17/2/24.
 */
public class PublicTokenRequest extends AbstractOAuthRequest<String> {

    public final static String PRA_COMPONENT_ACCESS_TOKEN = "component_access_token";

    public final static String BAY_COMPONENT_APPID = "component_appid";

    public final static String BAY_AUTHORIZATION_CODE = "authorization_code";

    public PublicTokenRequest() {
        super("https://api.weixin.qq.com/cgi-bin/component/api_query_auth");
    }

    @Override
    public String start() {
       HttpResponse response = HttpClientUtils.postSend(buildParams(),buildBody());
       if(HttpClientUtils.checkRespons(response)){
           return null;
       }
        try {
            return MzUtils.inputStreamToString(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
