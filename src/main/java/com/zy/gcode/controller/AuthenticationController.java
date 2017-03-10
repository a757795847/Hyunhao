package com.zy.gcode.controller;


import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.oauth.PublicLoginRequest;
import com.zy.gcode.oauth.PublicPreCodeRequest;
import com.zy.gcode.oauth.PublicTokenRequest;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.service.IAuthenticationService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String authAppid() {
        CodeRe<TokenConfig> codeRe = authenticationService.componetToekn();
        if (codeRe.isError()) {
            return "redirect:index.jsp?message=" + codeRe.getErrorMessage();
        }
        PublicPreCodeRequest preCodeRequest = new PublicPreCodeRequest();
        preCodeRequest.setParam(PublicPreCodeRequest.PRA_COMPONENT_ACCESS_TOKEN, codeRe.getMessage().getToken());
        preCodeRequest.setBody(PublicPreCodeRequest.BAY_COMPONENT_APPID, Constants.properties.getProperty("platform.appid"));
        PublicPreCodeRequest.PreAuthCode preAuthCode = preCodeRequest.start();
        if (preAuthCode.isError()) {
            return "redirect:index.jsp?message=" + preAuthCode.getErrmsg();
        }

        PublicLoginRequest loginRequest = new PublicLoginRequest();
        loginRequest.setParam(PublicLoginRequest.PRA_COMPONENT_APPID, Constants.properties.getProperty("platform.appid"))
                .setParam(PublicLoginRequest.PRA_PRE_AUTH_CODE, preAuthCode.getPreAuthCode())
                .setParam(PublicLoginRequest.PRA_REDIRECT_URI, "http://open.izhuiyou.com/auth/code");

        return "redirect:" + loginRequest.start();

    }

    @RequestMapping("auth/code")
    public
    @ResponseBody
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
        return "authorization success";

    }

    @RequestMapping("index")
    public
    @ResponseBody
    String index() {
        return HttpClientUtils.stringGetSend("https://api.weixin.qq.com/cgi-bin/user/get?access_token=eJSDxQpAum_UbOm5ijkvuyfhDfrQ8B1bF6tx-xVm3uA1Q7Z4zFflxWZmoDbgKgQUnQF6kHp9HI0wCrBHfV-5qi4nceVo6mUz3CW6kWXs2xAopPrUlF3zswTrgIRwQ-tKIKGjAJDQSO");
    }


}
