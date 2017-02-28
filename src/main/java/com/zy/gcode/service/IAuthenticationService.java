package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.pojo.WechatPublic;
import com.zy.gcode.pojo.WechatPublicServer;

/**
 * Created by admin5 on 17/1/21.
 */
public interface IAuthenticationService {
    CodeRe<WechatPublic> saveServerToken(String content, String token);
    CodeRe<TokenConfig> componetToekn();
    String decrpt(String msg_signature,String timestamp,String nonce,String str);
    WechatPublicServer getWechatPublic(String id);

    CodeRe<TokenConfig> getJsapiTicket();

    CodeRe<TokenConfig> getJsapiTicketByAppid(String appid);
    CodeRe<TokenConfig> getWxAccessToken(String appid);

}
