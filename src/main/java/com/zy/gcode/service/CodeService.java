package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.oauth.UserCodeOAuthRequest;
import com.zy.gcode.oauth.UserTokenOAuthRequest;
import com.zy.gcode.pojo.*;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.*;
import java.util.Map;

/**
 * Created by admin5 on 17/1/17.
 */
@Component
public class CodeService implements ICodeService {
    Logger log = LoggerFactory.getLogger(CodeService.class);


    public final static long CODE_EXPIRS=100;
    @Autowired
    PersistenceService persistenceService;

    @Autowired
    AuthenticationService authenticationService;

    public CodeRe code(String geappid, String url,String state) {

        CodeRe codeRe = new CodeRe();
        WechatPublic wechatPublic = persistenceService.get(WechatPublic.class, geappid);

        String wxappid;
        try {
            wxappid = wechatPublic.getWxAppid();
        } catch (NullPointerException e) {
            return CodeRe.error("wechatPublic is empty!");
        }


        GeCode geCode = new GeCode();
        String code = UniqueStringGenerator.getUniqueCode();
        geCode.setExpires(CODE_EXPIRS);
        geCode.setGeAppid(geappid);
        geCode.setGeCodeM(code);
        geCode.setState(state);
        try {
            geCode.setCallbackUrl(URLDecoder.decode(url,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return CodeRe.error("回调地址错误");
        }
        persistenceService.updateOrSave(geCode);


    /*    StringBuilder builder = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize").append("?");
        builder.append("appid=").append(wxappid);
        try {
            builder.append("&redirect_uri=").append(URLEncoder.encode(Constants.CALL_BACK_URL,"utf-8"));
            builder.append("&response_type=code&scope=").append(wechatPublic.getScope()).append("&state=").append(geappid);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return CodeRe.error("token回调地址有误!");
        }
        builder.append("&component_appid=").append(Constants.properties.get("platform.appid"));
        builder.append("#wechat_redirect");*/
        UserCodeOAuthRequest codeOAuthRequest = new UserCodeOAuthRequest();
            codeOAuthRequest.setParam(UserCodeOAuthRequest.APPID,wxappid);
        try {
            codeOAuthRequest.setParam(UserCodeOAuthRequest.REDIRECTURL,URLEncoder.encode(Constants.CALL_BACK_URL,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return CodeRe.error("该系统不支持utf-8");
        }
        codeOAuthRequest.setParam(UserCodeOAuthRequest.RESPONSETYPE,"code");
            codeOAuthRequest.setParam(UserCodeOAuthRequest.SCOPE, wechatPublic.getScope());
            codeOAuthRequest.setParam(UserCodeOAuthRequest.STATE,geappid);
            codeOAuthRequest.setParam(UserCodeOAuthRequest.COMPONENT_APPID,Constants.properties.get("platform.appid")+"#wechat_redirect");

        codeRe.setMessage(codeOAuthRequest.start());
        return codeRe;
    }

    public CodeRe token(String code, String state,String appid) {

        GeCode geCode = persistenceService.get(GeCode.class,state);
        try {
            geCode.getGeCodeM();
        } catch (NullPointerException e) {
           return CodeRe.error("app_id is not exist");
        }

        CodeRe<TokenConfig> componetTokenCodeRe =  authenticationService.componetToekn();
        if(componetTokenCodeRe.isError()){
            return  componetTokenCodeRe;
        }

       /* StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/component/access_token");
        builder.append("?appid=").append(appid);
        builder.append("&code=").append(code);
        builder.append("&grant_type=authorization_code")
                .append("&component_appid=").append(Constants.properties.getProperty("platform.appid"));
        builder.append("&component_access_token=").append(componetTokenCodeRe.getMessage().getToken());

        Map map = HttpClientUtils.mapGetSend(builder.toString());*/
        UserTokenOAuthRequest tokenOAuth = new UserTokenOAuthRequest();
        tokenOAuth.setParam(UserTokenOAuthRequest.APPID,appid);
        tokenOAuth.setParam(UserTokenOAuthRequest.CODE,code);
        tokenOAuth.setParam(UserTokenOAuthRequest.GRANT_TYPE,"authorization_code");
        tokenOAuth.setParam(UserTokenOAuthRequest.COMPONENT_APPID,Constants.properties.getProperty("platform.appid"));
        tokenOAuth.setParam(UserTokenOAuthRequest.COMPONENT_ACCESS_TOKEN,componetTokenCodeRe.getMessage().getToken());
        UserTokenOAuthRequest.AccessToken accessToken = tokenOAuth.start();

        if(accessToken == null){
            log.error("使用code 请求微信服务器失败！");
            return CodeRe.error("服务器异常,请稍后再试!");
        }
        if(accessToken.isError()){
            log.error(accessToken.getErrcode()+':'+accessToken.getErrmsg());
            return CodeRe.error("服务器异常");
        }

        WxToken wxToken = new WxToken();
       /* if(map.containsKey("errcode")){
            return CodeRe.error(map.get("errmsg").toString());
        }
        WxToken wxToken = new WxToken();
 *//*       &&map.containsKey("expires_in")&&map.containsKey("refresh_token")&&
                map.containsKey("openid")&&map.containsKey("scope")*//*
        if(map.containsKey("access_token")){
            wxToken.setWxToken(map.get("access_token").toString());
        }

        if(map.containsKey("expires_in")){
            wxToken.setExpires(Long.parseLong(map.get("expires_in").toString()));
        }

        if(map.containsKey("refresh_token")){
            wxToken.setRefreshToken(map.get("refresh_token").toString());
        }

        if(map.containsKey("openid")){
            wxToken.setUserOpenId(map.get("openid").toString());
        }
        if(map.containsKey("scope")){
            wxToken.setScope(map.get("scope").toString());
        }*/
        wxToken.setWxToken(accessToken.getAccessToken());
        wxToken.setExpires(accessToken.getExpiresIn().longValue());
        wxToken.setRefreshToken(accessToken.getRefreshToken());
        wxToken.setUserOpenId(accessToken.getOpenid());
        wxToken.setScope(accessToken.getScope());
        persistenceService.updateOrSave(wxToken);
        geCode.setOpenid(wxToken.getUserOpenId());
        geCode.setWxToken(wxToken.getWxToken());
        persistenceService.updateOrSave(geCode);
        if(StringUtils.isEmpty(wxToken.getWxToken())&&StringUtils.isEmpty(wxToken.getUserOpenId())){
            return  CodeRe.error("token or openid is empty");
        }

       WechatUserInfo wechatUserInfo = user(wxToken.getWxToken(),wxToken.getUserOpenId(),appid);

        if(wechatUserInfo ==null){
            return CodeRe.error("to get wechatUserInfo from weixin is fail");
        }
        return CodeRe.correct(geCode.getCallbackUrl()+"?code="+geCode.getGeCodeM()+"&state="+geCode.getState()+"&zyid="+geCode.getGeAppid());
    }

    private WechatUserInfo user(String token, String openid, String appid){
        StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?access_token=")
        .append(token).append("&openid=").append(openid).append("&lang=zh_CN");
        Map map = HttpClientUtils.mapGetSend(builder.toString());
        if(map == null){
            return  null;
        }

        WechatUserInfo wechatUserInfo = new WechatUserInfo();
        wechatUserInfo.setOpenId(openid);

        if(map.containsKey("nickname")){
            wechatUserInfo.setNick(map.get("nickname").toString());
        }

        if(map.containsKey("sex")){
            wechatUserInfo.setSex(map.get("sex").toString());
        }

        if(map.containsKey("province")){
            wechatUserInfo.setProvince(map.get("province").toString());
        }
        if(map.containsKey("city")){
            wechatUserInfo.setCity(map.get("city").toString());
        }
        if(map.containsKey("country")){
            wechatUserInfo.setCountry(map.get("country").toString());
        }
        if(map.containsKey("headimgurl")){
            wechatUserInfo.setHeadImgUrl(map.get("headimgurl").toString());
        }
        if(map.containsKey("privilege")){
            wechatUserInfo.setPrivilege(map.get("privilege").toString());
        }
        if(map.containsKey("unionid")){
            wechatUserInfo.setUnionId(map.get("unionid").toString());
        }
        wechatUserInfo.setAppid(appid);
        persistenceService.updateOrSave(wechatUserInfo);

        return wechatUserInfo;
    }

    @Override
    public CodeRe<GeToken> geToken(String geCodeM,String geappid) {
        GeCode geCode =persistenceService.get(GeCode.class,geappid);

        Timestamp updatetime;
        try {
            updatetime = geCode.getUpdateTime();
        } catch (NullPointerException e) {
           return CodeRe.error("invalid appid");
        }
        if(!geCode.getGeCodeM().equals(geCodeM.trim())){
            return CodeRe.error("invalid geCodeM");
        }
        if(DateUtils.isOutOfDate(updatetime,geCode.getExpires())){
            return CodeRe.error("time out gecode");
        }

        String geTokenM =  UniqueStringGenerator.getUniqueToken();
        GeToken geToken = new GeToken();
        geToken.setGeCodeM(geCodeM);
        geToken.setGeTokenM(geTokenM);
        geToken.setWxToken(geCode.getWxToken());
        geToken.setOpenid(geCode.getOpenid());
        geToken.setGeAppid(geappid);
        persistenceService.updateOrSave(geToken);
        return CodeRe.correct(geToken);
    }

    @Override
    public CodeRe<WechatUserInfo> getUser(String zyid, String token) {
       GeToken geToken = persistenceService.get(GeToken.class,zyid);
        Timestamp updatetime;

        try {
            updatetime = geToken.getUpdateTime();
        } catch (NullPointerException e) {
           return CodeRe.error("token is invalid");
        }
        if(!geToken.getGeTokenM().equals(token))
            return CodeRe.error("token is error");

        if(DateUtils.isOutOfDate(updatetime,7000L)){
           return CodeRe.error("token out time");
       }

       String openid = geToken.getOpenid();

     WechatUserInfo wechatUserInfo = persistenceService.get(WechatUserInfo.class,openid);

        try {
            wechatUserInfo.getUpdateTime();
        } catch (NullPointerException e) {
          return CodeRe.error("openid is invalid");
        }

        return CodeRe.correct(wechatUserInfo);
    }

}
