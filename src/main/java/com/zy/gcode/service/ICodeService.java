package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.GeToken;
import com.zy.gcode.pojo.WechatUserInfo;

/**
 * Created by admin5 on 17/1/17.
 */
public interface ICodeService {
    CodeRe code(String appid, String url,String state);
    CodeRe token(String code, String state,String appid);
    CodeRe<GeToken> geToken(String geCode, String geappid);
    CodeRe<WechatUserInfo> getUser(String zyid, String token);
}
