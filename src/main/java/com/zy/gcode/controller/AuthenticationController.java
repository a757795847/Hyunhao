package com.zy.gcode.controller;


import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.oauth.PublicLoginRequest;
import com.zy.gcode.oauth.PublicPreCodeRequest;
import com.zy.gcode.oauth.PublicTokenRequest;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.service.intef.IAuthenticationService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.Du;
import com.zy.gcode.utils.JwtUtils;
import com.zy.gcode.utils.SubjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin5 on 17/1/18.
 */
@Controller
public class AuthenticationController {

    @Autowired
    IAuthenticationService authenticationService;

    private Map<String, String> authCache = new ConcurrentHashMap<>();
    private Map<String, String> authCacheFacade = new LinkedHashMap() {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            if (size() > 100) {
                authCache.remove(eldest.getKey());
                return true;
            }
            return false;
        }
    };

    @RequestMapping("auth/receive")
    public
    @ResponseBody
    String getGeCode(String msg_signature, String timestamp, String nonce, @RequestBody(required = false) String str) {
        authenticationService.decrpt(msg_signature, timestamp, nonce, str);
        return "success";
    }

    @RequestMapping("auth/index")
    public
    @ResponseBody
    Object authAppid(@RequestParam String url, HttpServletRequest request) {
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
        String authentication = request.getHeader(JwtUtils.AUTHORIZATION);
        PublicLoginRequest loginRequest = new PublicLoginRequest();
        loginRequest.setParam(PublicLoginRequest.PRA_COMPONENT_APPID, Constants.properties.getProperty("platform.appid"))
                .setParam(PublicLoginRequest.PRA_PRE_AUTH_CODE, preAuthCode.getPreAuthCode())
                .setParam(PublicLoginRequest.PRA_REDIRECT_URI, "http://open.izhuiyou.com/auth2/code?username=" + SubjectUtils.getUserName());
        authCache.put(SubjectUtils.getUserName(), authentication);
        authCacheFacade.put(SubjectUtils.getUserName(), authentication + ":" + url);
        return ControllerStatus.ok(loginRequest.start());

    }

    @RequestMapping("auth2/code")
    public String serverCode(String auth_code, String expires_in, String username, HttpServletResponse response) {
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

        String redirectInfo = authCache.get(username);
        String[] infos = redirectInfo.split(":");
        response.setHeader(JwtUtils.AUTHORIZATION, infos[0]);
        return "redirect:" + infos[1];

    }

    @RequestMapping("index")
    public
    @ResponseBody
    String index(String param) {
        Jedis jedis = new Jedis("localhost", 6379);
        Du.pl(jedis.get("key"));
        return jedis.get(param);
    }


}
