package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.AppInterface;
import com.zy.gcode.pojo.AuthorizationInfo;
import com.zy.gcode.pojo.ComponetToken;

import java.util.List;

/**
 * Created by admin5 on 17/1/21.
 */
public interface IAuthenticationService {
    CodeRe<AuthorizationInfo> saveServerToken(String content,String token);
    CodeRe<ComponetToken> componetToekn();
    String decrpt(String msg_signature,String timestamp,String nonce,String str);
    AppInterface  allAppInterface(String id);

}
