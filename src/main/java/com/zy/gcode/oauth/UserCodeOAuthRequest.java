package com.zy.gcode.oauth;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;

/**
 * Created by admin5 on 17/2/23.
 */
public class UserCodeOAuthRequest extends AbstractOAuthRequest {
    private Entry appid = new Entry("appid");
    private Entry redirectUrl = new Entry("redirect_uri");
    private Entry responseType = new Entry("response_type");
    private Entry scope = new Entry("scope");
    private Entry state = new Entry("state");

    public UserCodeOAuthRequest(){
        super("https://open.weixin.qq.com/connect/oauth2/authorize");
    }

    public String getAppid() {
        return appid.value;
    }

    public void setAppid(String appid) {
        this.appid.value = appid;
    }

    public String getRedirectUrl() {
        return redirectUrl.value;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl.value = redirectUrl;
    }

    public String getResponseType() {
        return responseType.value;
    }

    public void setResponseType(String responseType) {
        this.responseType.value = responseType;
    }

    public String getScope() {
        return scope.value;
    }

    public void setScope(String scope) {
        this.scope.value = scope;
    }

    public String getState() {
        return state.value;
    }

    public void setState(String state) {
        this.state.value = state;
    }

    @Override
    public String build() {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?").append(appid.name).append("=").append(appid.value);

        return null;
    }

}
