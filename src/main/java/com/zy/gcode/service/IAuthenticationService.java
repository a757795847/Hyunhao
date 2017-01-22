package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;

/**
 * Created by admin5 on 17/1/21.
 */
public interface IAuthenticationService {
    public CodeRe saveServerToken(String content);
}
