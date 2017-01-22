package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.ComponetToken;

/**
 * Created by admin5 on 17/1/21.
 */
public interface IAuthenticationService {
    CodeRe saveServerToken(String content);
    CodeRe<ComponetToken> componetToekn();
    String decrpt(String msg_signature,String timestamp,String nonce,String str);
}
