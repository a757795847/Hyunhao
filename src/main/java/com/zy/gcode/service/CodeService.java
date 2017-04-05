package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.oauth.UserCodeOAuthRequest;
import com.zy.gcode.oauth.UserInfoOAuthRequest;
import com.zy.gcode.oauth.UserTokenOAuthRequest;
import com.zy.gcode.pojo.*;
import com.zy.gcode.service.intef.ICodeService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.TappidUtils;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Created by admin5 on 17/1/17.
 */
@Component
public class CodeService implements ICodeService {
    Logger log = LoggerFactory.getLogger(CodeService.class);


    public final static long CODE_EXPIRS = 100;
    @Autowired
    PersistenceService persistenceService;

    @Autowired
    AuthenticationService authenticationService;

    @Transactional
    public CodeRe code(String tappid, String url, String state) {
        TappidUtils.TappidEntry tappidEntry = TappidUtils.deTappid(tappid);
        UserConfig userConfig = persistenceService.get(UserConfig.class, tappidEntry.getUserConfigId());


        if (userConfig == null) {
            return CodeRe.error("wechatPublicServer is empty!");
        }


        GeCode geCode = new GeCode();
        String code = UniqueStringGenerator.getUniqueCode();
        geCode.setExpires(CODE_EXPIRS);
        geCode.setGeAppid(tappid);
        geCode.setGeCodeM(code);
        geCode.setState(state);
        try {
            geCode.setCallbackUrl(URLDecoder.decode(url, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return CodeRe.error("回调地址错误");
        }
        persistenceService.updateOrSave(geCode);
        UserCodeOAuthRequest codeOAuthRequest = new UserCodeOAuthRequest();
        codeOAuthRequest.setParam(UserCodeOAuthRequest.APPID, userConfig.getWechatOfficialId());
        try {
            codeOAuthRequest.setParam(UserCodeOAuthRequest.REDIRECTURL, URLEncoder.encode(Constants.CALL_BACK_URL, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return CodeRe.error("该系统不支持utf-8");
        }
        codeOAuthRequest.setParam(UserCodeOAuthRequest.RESPONSETYPE, "code");
        codeOAuthRequest.setParam(UserCodeOAuthRequest.SCOPE, "snsapi_userinfo");
        codeOAuthRequest.setParam(UserCodeOAuthRequest.STATE, code);
        codeOAuthRequest.setParam(UserCodeOAuthRequest.COMPONENT_APPID, (String) Constants.properties.get("platform.appid")).setSuffix("#wechat_redirect");
        return CodeRe.correct(codeOAuthRequest.start());
    }

    @Transactional
    public CodeRe token(String code, String state, String appid) {

        GeCode geCode = persistenceService.get(GeCode.class, state);
        if (geCode == null) {
            return CodeRe.error("app_id is not exist");
        }

        CodeRe<TokenConfig> componetTokenCodeRe = authenticationService.componetToekn();
        if (componetTokenCodeRe.isError()) {
            return componetTokenCodeRe;
        }
        UserTokenOAuthRequest tokenOAuth = new UserTokenOAuthRequest();
        tokenOAuth.setParam(UserTokenOAuthRequest.APPID, appid)
                .setParam(UserTokenOAuthRequest.CODE, code)
                .setParam(UserTokenOAuthRequest.GRANT_TYPE, "authorization_code")
                .setParam(UserTokenOAuthRequest.COMPONENT_APPID, Constants.properties.getProperty("platform.appid"))
                .setParam(UserTokenOAuthRequest.COMPONENT_ACCESS_TOKEN, componetTokenCodeRe.getMessage().getToken());
        UserTokenOAuthRequest.AccessToken accessToken = tokenOAuth.start();

        if (accessToken == null) {
            log.error("使用code 请求微信服务器失败！");
            return CodeRe.error("服务器异常,请稍后再试!");
        }
        if (accessToken.isError()) {
            log.error(accessToken.getErrcode() + ':' + accessToken.getErrmsg());
            return CodeRe.error("服务器异常");
        }
        WxToken wxToken = new WxToken();
        wxToken.setWxToken(accessToken.getAccessToken());
        wxToken.setExpires(accessToken.getExpiresIn().longValue());
        wxToken.setRefreshToken(accessToken.getRefreshToken());
        wxToken.setUserOpenId(accessToken.getOpenid());
        wxToken.setScope(accessToken.getScope());
        persistenceService.updateOrSave(wxToken);
        //更新geCode 的openid值和wxToken值,翻遍客户端用gecode获取getoken时设定geToken属性
        geCode.setOpenid(wxToken.getUserOpenId());
        geCode.setWxToken(wxToken.getWxToken());
        persistenceService.updateOrSave(geCode);
        if (StringUtils.isEmpty(wxToken.getWxToken()) && StringUtils.isEmpty(wxToken.getUserOpenId())) {
            return CodeRe.error("token or openid is empty");
        }

        WechatUserInfo wechatUserInfo = user(wxToken.getWxToken(), wxToken.getUserOpenId(), appid, geCode.getGeAppid());

        if (wechatUserInfo == null) {
            return CodeRe.error("to get wechatUserInfo from weixin is fail");
        }
        return CodeRe.correct(geCode.getCallbackUrl() + "?code=" + geCode.getGeCodeM() + "&state=" + geCode.getState() + "&zyid=" + geCode.getGeAppid());
    }

    @Transactional
    private WechatUserInfo user(String token, String openid, String appid, String tappid) {
        WechatUserInfo wechatUserInfo1 = persistenceService.get(WechatUserInfo.class, openid);
        if (wechatUserInfo1 != null) {
            return wechatUserInfo1;
        }

        UserInfoOAuthRequest infoOAuthRequest = new UserInfoOAuthRequest();
        infoOAuthRequest.setParam(UserInfoOAuthRequest.ACCESS_TOKEN, token)
                .setParam(UserInfoOAuthRequest.OPENID, openid).setSuffix("&lang=zh_CN").start();
        UserInfoOAuthRequest.UserInfo userInfo = infoOAuthRequest.start();
        if (userInfo == null) {
            return null;
        }
        if (userInfo.isError()) {
            log.error("微信服务异常:" + userInfo.getErrmsg());
            return null;
        }
        WechatUserInfo wechatUserInfo = new WechatUserInfo();
        wechatUserInfo.setOpenId(userInfo.getOpenid());
        wechatUserInfo.setNick(userInfo.getNickname());
        wechatUserInfo.setSex(userInfo.getSex());
        wechatUserInfo.setProvince(userInfo.getProvince());
        wechatUserInfo.setCity(userInfo.getCity());
        wechatUserInfo.setCountry(userInfo.getCountry());
        wechatUserInfo.setHeadImgUrl(userInfo.getHeadimgurl());
        wechatUserInfo.setPrivilege(Arrays.toString(userInfo.getPrivilege()));
        wechatUserInfo.setUnionId(userInfo.getUnionid());

        wechatUserInfo.setAppid(appid);
        wechatUserInfo.setTappid(tappid);
        persistenceService.updateOrSave(wechatUserInfo);

        return wechatUserInfo;
    }

    @Override
    @Transactional
    public CodeRe<GeToken> geToken(String geCodeM, String tappid) {
        GeCode geCode = persistenceService.get(GeCode.class, geCodeM);

        Timestamp updatetime;
        try {
            updatetime = geCode.getUpdateTime();
        } catch (NullPointerException e) {
            return CodeRe.error("invalid appid");
        }
        if (!geCode.getGeCodeM().equals(geCodeM.trim())) {
            return CodeRe.error("invalid geCodeM");
        }
        if (DateUtils.isOutOfDate(updatetime, geCode.getExpires())) {
            return CodeRe.error("time out gecode");
        }

        String geTokenM = UniqueStringGenerator.getUniqueToken();
        GeToken geToken = new GeToken();
        geToken.setGeCodeM(geCodeM);
        geToken.setGeTokenM(geTokenM);
        geToken.setWxToken(geCode.getWxToken());
        geToken.setOpenid(geCode.getOpenid());
        geToken.setGeAppid(tappid);
        persistenceService.updateOrSave(geToken);
        return CodeRe.correct(geToken);
    }

    @Override
    @Transactional
    public CodeRe<WechatUserInfo> getUser(String zyid, String token) {
        GeToken geToken = persistenceService.get(GeToken.class, token);
        Timestamp updatetime;

        try {
            updatetime = geToken.getUpdateTime();
        } catch (NullPointerException e) {
            return CodeRe.error("token is invalid");
        }
        if (!geToken.getGeTokenM().equals(token))
            return CodeRe.error("token is error");

        if (DateUtils.isOutOfDate(updatetime, 7000L)) {
            return CodeRe.error("token out time");
        }

        String openid = geToken.getOpenid();

        WechatUserInfo wechatUserInfo = persistenceService.get(WechatUserInfo.class, openid);

        try {
            wechatUserInfo.getUpdateTime();
        } catch (NullPointerException e) {
            return CodeRe.error("openid is invalid");
        }

        return CodeRe.correct(wechatUserInfo);
    }

}
