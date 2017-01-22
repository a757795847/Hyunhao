package com.zy.gcode.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.*;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Map;

/**
 * Created by admin5 on 17/1/17.
 */
@Component
public class CodeService implements ICodeService {
    public final static long CODE_EXPIRS=100;
    public final static String CALL_BACK_URL="http://open.izhuiyou.com/code/userinfo";
    public final static String GE_CODE="https://open.weixin.qq.com/connect/oauth2/authorize";
    //public final static String GE_CODE="https://localhost:8443/zy/authentication/geCode";

    public static ObjectMapper objectMapper = new ObjectMapper();

    public final static String NULL_APP="null_app";
    public final static String ILLEGAL_STATUS="illegal_status";
    public final static String INVALID_GECODE="invalid_gecode";
    public final static String TIME_OUT_GECODE="time_out_gecode";

    @Autowired
    PersistenceService persistenceService;

    public CodeRe code(String geappid, String url) {
        CodeRe codeRe = new CodeRe();
        AppInterface appInterface = persistenceService.get(AppInterface.class, geappid);
        if(appInterface==null){
            codeRe.setError(NULL_APP);
            return codeRe;
        }
        GeCode geCode = new GeCode();
        String code = UniqueStringGenerator.getUniqueCode();
        geCode.setExpires(CODE_EXPIRS);
        geCode.setGeAppid(geappid);
        geCode.setGeCodeM(code);
        geCode.setCallbackUrl(url);
        persistenceService.updateOrSave(geCode);


        StringBuilder builder = new StringBuilder(GE_CODE).append("?");
        builder.append("appid=").append(appInterface.getWxAppid());
        try {
            builder.append("&redirect_uri=").append(URLEncoder.encode(CALL_BACK_URL,"utf-8"));
            builder.append("&response_type=code&scope=").append(appInterface.getScope()).append("&state=").append(geCode.getGeCodeM());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        builder.append("&component_appid=wxa8febcce6444f95f");
        builder.append("#wechat_redirect");
        codeRe.setMessage(builder.toString());
        return codeRe;
    }

//https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
    public CodeRe token(String code, String state,String appid) {//state gecodeM
        CodeRe codeRe = new CodeRe();
      // AppInterface appInterface = persistenceService.load(AppInterface.class,state);
        AuthorizationInfo info = persistenceService.load(AuthorizationInfo.class,"wxa8febcce6444f95f");
        if(info==null){
           return CodeRe.error("authorization is empety!");
        }
        GeCode geCode = persistenceService.get(GeCode.class,state);
        if(info.getUpdateTime().before(new Date(System.currentTimeMillis()-(info.getExpiresIn()*1000)))){
            StringBuilder builder = new StringBuilder("http://mp.weixin.qq.com/wiki/2/88b2bf1265a707c031e51f26ca5e6512.html")
                    .append("?component_access_token=").append(info.getAuthorizerAccessToken());
           Map<String,Object> map = HttpClientUtils.MapSSLPostSend(builder.toString(),"{\n" +
                    "\"component_appid\":\"wxa8febcce6444f95f\",\n" +
                    "\"authorizer_appid\":\""+appid+"\",\n" +
                    "\"authorizer_refresh_token\":\""+info.getAuthorizerRefreshToken()+"\",\n" +
                    "}");
           info.setAuthorizerRefreshToken(map.get("authorizer_refresh_token").toString());
           info.setAuthorizerAccessToken(map.get("authorizer_access_token").toString());
           info.setExpiresIn(Long.parseLong(map.get("expires_in").toString()));
           persistenceService.updateOrSave(info);
        }


        StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/component/access_token");
        builder.append("?appid=").append(appid);
        builder.append("&code=").append(code);
        builder.append("&grant_type=authorization_code")
                .append("&component_appid=wxa8febcce6444f95f");
        builder.append("&component_access_token=").append(info.getAuthorizerAccessToken());

        HttpResponse httpResponse = HttpClientUtils.SSLGetSend(builder.toString());
       if(httpResponse.getStatusLine().getStatusCode()!=200){
            codeRe.setError(ILLEGAL_STATUS);
            return codeRe;
        }

    /*    { "access_token":"ACCESS_TOKEN",
                "expires_in":7200,
                "refresh_token":"REFRESH_TOKEN",
                "openid":"OPENID",
                "scope":"SCOPE" }*/
        Map<String,Object> map = null;
        try{
            map = objectMapper.readValue(httpResponse.getEntity().getContent(), Map.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(map.containsKey("errcode")){
            codeRe.setError(map.get("errcode").toString());
            return codeRe;
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
            codeRe.setError("token or openid is empty");
            return  codeRe;
        }

       User user = user(wxToken.getWxToken(),wxToken.getUserOpenId());

        if(user ==null){
            codeRe.setError("to get user from weixin is fail");
            return codeRe;
        }

        codeRe.setMessage(geCode.getCallbackUrl()+"?code="+geCode.getGeCodeM());
        return codeRe;
    }

    private User user(String token, String openid){
        StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?access_token=")
        .append(token).append("&openid=").append(openid).append("&lang=zh_CN");
        HttpResponse httpResponse = HttpClientUtils.SSLGetSend(builder.toString());
        Map<String,Object> map = null;
        try {
            map = objectMapper.readValue(httpResponse.getEntity().getContent(),Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(map.containsKey("errcode")){
            return null;
        }
      /*  {    "openid":" OPENID",
                " nickname": NICKNAME,
                "sex":"1",
                "province":"PROVINCE"
            "city":"CITY",
                "country":"COUNTRY",
                "headimgurl":    "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ
            4eMsv84eavHiaiceqxibJxCfHe/46",
            "privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
            "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
        }*/
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
    public CodeRe<String> geToken(String geCodeM) {
        CodeRe<String> codeRe = new CodeRe<>();
        GeCode geCode =persistenceService.get(GeCode.class,geCodeM);
        if(geCode==null){
            codeRe.setError(INVALID_GECODE);
            return codeRe;
        }
        if(geCode.getUpdateTime().before(new Date(System.currentTimeMillis()-geCode.getExpires()*1000))){
            codeRe.setError(TIME_OUT_GECODE);
            return codeRe;
        }

        String geTokenM =  UniqueStringGenerator.getUniqueToken();
        GeToken geToken = new GeToken();
        geToken.setGeCodeM(geCodeM);
        geToken.setGeTokenM(geTokenM);
        geToken.setWxToken(geCode.getWxToken());
        geToken.setOpenid(geCode.getOpenid());
        persistenceService.updateOrSave(geToken);
        codeRe.setMessage(geTokenM);
        return codeRe;
    }

    @Override
    public CodeRe<User> getUser(String token) {
        CodeRe<User> codeRe = new CodeRe<>();
       GeToken geToken = persistenceService.get(GeToken.class,token);


       if(geToken == null){
            codeRe.setError("token is invalid!");
            return  codeRe;
       }
       if(new Date(geToken.getUpdateTime().getTime()).after(new Date(System.currentTimeMillis()+7000*1000))){
            codeRe.setError("token_out_time");
            return codeRe;
       }

       String openid = geToken.getOpenid();
       if(StringUtils.isEmpty(openid)) {
           codeRe.setError("openid is empty");
           return codeRe;
       }

     User user = persistenceService.get(User.class,openid);
        codeRe.setMessage(user);
        return codeRe;
    }

}
