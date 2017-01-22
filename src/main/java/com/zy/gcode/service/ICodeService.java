package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.User;

/**
 * Created by admin5 on 17/1/17.
 */
public interface ICodeService {
    CodeRe code(String appid, String url);
    CodeRe token(String code, String state);
    CodeRe<String> geToken(String geCode);
    CodeRe<User> getUser(String token);
}
