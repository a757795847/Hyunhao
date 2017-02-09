package com.zy.gcode.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.gcode.controller.AuthenticationController;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.*;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.*;
import java.util.Date;
import java.util.Map;

/**
 * Created by admin5 on 17/1/17.
 */
@Component
public class CodeService implements ICodeService {
    public final static long CODE_EXPIRS=100;
    @Autowired
    PersistenceService persistenceService;

    @Autowired
    AuthenticationService authenticationService;

    public CodeRe code(String geappid, String url) {
        CodeRe codeRe = new CodeRe();
        AppInterface appInterface = persistenceService.get(AppInterface.class, geappid);

        String wxappid;
        try {
            wxappid = appInterface.getWxAppid();
        } catch (NullPointerException e) {
            return CodeRe.error("appInterface is empty!");
        }


        GeCode geCode = new GeCode();
        String code = UniqueStringGenerator.getUniqueCode();
        geCode.setExpires(CODE_EXPIRS);
        geCode.setGeAppid(geappid);
        geCode.setGeCodeM(code);
        try {
            geCode.setCallbackUrl(URLDecoder.decode(url,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return CodeRe.error("回调地址错误");
        }
        persistenceService.updateOrSave(geCode);


        StringBuilder builder = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize").append("?");
        builder.append("appid=").append(wxappid);
        try {
            builder.append("&redirect_uri=").append(URLEncoder.encode(Constants.CALL_BACK_URL,"utf-8"));
            builder.append("&response_type=code&scope=").append(appInterface.getScope()).append("&state=").append(geappid);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return CodeRe.error("token回调地址有误!");
        }
        builder.append("&component_appid=").append(Constants.properties.get("platform.appid"));
        builder.append("#wechat_redirect");
        codeRe.setMessage(builder.toString());
        return codeRe;
    }

    public CodeRe token(String code, String state,String appid) {//state gecodeM

        GeCode geCode = persistenceService.get(GeCode.class,state);
        try {
            geCode.getGeCodeM();
        } catch (NullPointerException e) {
           return CodeRe.error("app_id is not exist");
        }

        CodeRe<ComponetToken> componetTokenCodeRe =  authenticationService.componetToekn();
        if(componetTokenCodeRe.isError()){
            return  componetTokenCodeRe;
        }

        StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/component/access_token");
        builder.append("?appid=").append(appid);
        builder.append("&code=").append(code);
        builder.append("&grant_type=authorization_code")
                .append("&component_appid=").append(Constants.properties.getProperty("platform.appid"));
        builder.append("&component_access_token=").append(componetTokenCodeRe.getMessage().getComponentAccessToken());

        Map map = HttpClientUtils.mapSSLGetSend(builder.toString());
        if(map == null){
            return CodeRe.error("status error!");
        }


        if(map.containsKey("errcode")){
            return CodeRe.error(map.get("errmsg").toString());
        }
        WxToken wxToken = new WxToken();
 /*       &&map.containsKey("expires_in")&&map.containsKey("refresh_token")&&
                map.containsKey("openid")&&map.containsKey("scope")*/
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
        }
        persistenceService.updateOrSave(wxToken);
        geCode.setOpenid(wxToken.getUserOpenId());
        geCode.setWxToken(wxToken.getWxToken());
        persistenceService.updateOrSave(geCode);
        if(StringUtils.isEmpty(wxToken.getWxToken())&&StringUtils.isEmpty(wxToken.getUserOpenId())){
            return  CodeRe.error("token or openid is empty");
        }

       User user = user(wxToken.getWxToken(),wxToken.getUserOpenId());

        if(user ==null){
            return CodeRe.error("to get user from weixin is fail");
        }
        return CodeRe.correct(geCode.getCallbackUrl()+"?code="+geCode.getGeCodeM());
    }

    private User user(String token, String openid){
        StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?access_token=")
        .append(token).append("&openid=").append(openid).append("&lang=zh_CN");
        Map map = HttpClientUtils.mapSSLGetSend(builder.toString());
        if(map == null){
            return  null;
        }

        User user = new User();
        user.setOpenId(openid);

        if(map.containsKey("nickname")){
            user.setNick(map.get("nickname").toString());
        }

        if(map.containsKey("sex")){
            user.setSex(map.get("sex").toString());
        }

        if(map.containsKey("province")){
            user.setProvince(map.get("province").toString());
        }
        if(map.containsKey("city")){
            user.setCity(map.get("city").toString());
        }
        if(map.containsKey("country")){
            user.setCountry(map.get("country").toString());
        }
        if(map.containsKey("headimgurl")){
            user.setHeadImgUrl(map.get("headimgurl").toString());
        }
        if(map.containsKey("privilege")){
            user.setPrivilege(map.get("privilege").toString());
        }
        if(map.containsKey("unionid")){
            user.setUnionId(map.get("unionid").toString());
        }
        persistenceService.updateOrSave(user);

        return user;
    }

    @Override
    public CodeRe<String> geToken(String geCodeM,String geappid) {
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
        persistenceService.updateOrSave(geToken);
        return CodeRe.correct(geTokenM);
    }

    @Override
    public CodeRe<User> getUser(String token) {
       GeToken geToken = persistenceService.get(GeToken.class,token);
        Timestamp updatetime;

        try {
            updatetime = geToken.getUpdateTime();
        } catch (NullPointerException e) {
           return CodeRe.error("token is invalid");
        }


        if(DateUtils.isOutOfDate(updatetime,7000L)){
           return CodeRe.error("token out time");
       }

       String openid = geToken.getOpenid();

     User user = persistenceService.get(User.class,openid);

        try {
            user.getUpdateTime();
        } catch (NullPointerException e) {
          return CodeRe.error("openid is invalid");
        }

        return CodeRe.correct(user);
    }

}
