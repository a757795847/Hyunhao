package com.zy.gcode.service.intef;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.pojo.WechatPublic;
import com.zy.gcode.pojo.WechatPublicServer;

/**
 * Created by admin5 on 17/1/21.
 */
public interface IAuthenticationService {
    CodeRe<WechatPublic> saveServerToken(String content, String token);

    CodeRe getAuthorizerToken(String appid);

    CodeRe<TokenConfig> componetToekn();

    String decrpt(String msg_signature, String timestamp, String nonce, String str);



    CodeRe<TokenConfig> getJsapiTicket();

    CodeRe<String> getJsapiTicketByAppid(String appid);

    CodeRe<TokenConfig> getWxAccessTokenBySecret(String appid);

}
