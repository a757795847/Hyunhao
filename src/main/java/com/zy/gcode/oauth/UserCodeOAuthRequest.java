package com.zy.gcode.oauth;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin5 on 17/2/23.
 */
public class UserCodeOAuthRequest extends AbstractOAuthRequest<String> {
  /*  private Entry appid = new Entry("appid");
    private Entry redirectUrl = new Entry("redirect_uri");
    private Entry responseType = new Entry("response_type");
    private Entry scope = new Entry("scope");
    private Entry state = new Entry("state");*/

    public static final String APPID = "appid";
    public static final String REDIRECTURL = "redirect_uri";
    public static final String RESPONSETYPE = "response_type";
    public static final String SCOPE = "scope";
    public static final String STATE = "state";
    public UserCodeOAuthRequest(){
        super("https://open.weixin.qq.com/connect/oauth2/authorize");
    }

    @Override
    public String start() {
       return buildParams();
    }
}
