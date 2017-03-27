package com.zy.gcode.controller;


import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.oauth.PublicLoginRequest;
import com.zy.gcode.oauth.PublicPreCodeRequest;
import com.zy.gcode.oauth.PublicTokenRequest;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.service.IAuthenticationService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by admin5 on 17/1/18.
 */
@Controller
public class AuthenticationController {

    @Autowired
    IAuthenticationService authenticationService;


    @RequestMapping("auth/receive")
    public
    @ResponseBody
    String getGeCode(String msg_signature, String timestamp, String nonce, @RequestBody(required = false) String str) {
        authenticationService.decrpt(msg_signature, timestamp, nonce, str);
        return "success";
    }

    @RequestMapping("auth/index")
    public @ResponseBody Object authAppid(@RequestParam String url, HttpServletRequest request) {
        CodeRe<TokenConfig> codeRe = authenticationService.componetToekn();
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        PublicPreCodeRequest preCodeRequest = new PublicPreCodeRequest();
        preCodeRequest.setParam(PublicPreCodeRequest.PRA_COMPONENT_ACCESS_TOKEN, codeRe.getMessage().getToken());
        preCodeRequest.setBody(PublicPreCodeRequest.BAY_COMPONENT_APPID, Constants.properties.getProperty("platform.appid"));
        PublicPreCodeRequest.PreAuthCode preAuthCode = preCodeRequest.start();
        if (preAuthCode.isError()) {
            return ControllerStatus.error(preAuthCode.getErrmsg());
        }
        String authorization = request.getHeader("authorization");

        PublicLoginRequest loginRequest = new PublicLoginRequest();
        loginRequest.setParam(PublicLoginRequest.PRA_COMPONENT_APPID, Constants.properties.getProperty("platform.appid"))
                .setParam(PublicLoginRequest.PRA_PRE_AUTH_CODE, preAuthCode.getPreAuthCode())
                .setParam(PublicLoginRequest.PRA_REDIRECT_URI, "http://open.izhuiyou.com/auth/code?jwt="+authorization);
        try {
            SecurityUtils.getSubject().getSession().setAttribute("url", URLDecoder.decode(url,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ControllerStatus.error();
        }
        return ControllerStatus.ok(loginRequest.start());

    }

    @RequestMapping("auth/code")
    public
    String serverCode(String auth_code, String expires_in) {
        CodeRe<TokenConfig> componetTokenCodeRe = authenticationService.componetToekn();
        if (componetTokenCodeRe.isError()) {
            return componetTokenCodeRe.getErrorMessage();
        }
        PublicTokenRequest tokenRequest = new PublicTokenRequest();
        tokenRequest.setParam(PublicTokenRequest.PRA_COMPONENT_ACCESS_TOKEN, componetTokenCodeRe.getMessage().getToken())
                .setBody(PublicTokenRequest.BAY_COMPONENT_APPID, Constants.properties.getProperty("platform.appid"))
                .setBody(PublicTokenRequest.BAY_AUTHORIZATION_CODE, auth_code);

        CodeRe codeRe = authenticationService.saveServerToken(tokenRequest.start().toString(), componetTokenCodeRe.getMessage().getToken());
        if (codeRe.isError()) {
            return codeRe.getErrorMessage();
        }
        String url = JwtUtils.claimAsString("url");
        return "redirect:"+url;

    }

 /*   @RequestMapping("index")
    public
    @ResponseBody
    String index() {
        return HttpClientUtils. stringGetSend("https://api.weixin.qq.com/cgi-bin/user/get?access_token=eJSDxQpAum_UbOm5ijkvuyfhDfrQ8B1bF6tx-xVm3uA1Q7Z4zFflxWZmoDbgKgQUnQF6kHp9HI0wCrBHfV-5qi4nceVo6mUz3CW6kWXs2xAopPrUlF3zswTrgIRwQ-tKIKGjAJDQSO")
        + HttpClientUtils. stringGetSend("https://api.weixin.qq.com/cgi-bin/user/get?access_token=uwCEJnv3DVFcipf9h_u6YQSn4AVGc99NU4Df7cEPo4wq0udJKueRFWknkZuN6h6S4zYwewLTzu4PDg5nchB9RAjZ4om8WB16MMnzcUQ779UZWXjADAIFG");
    }*/


}
