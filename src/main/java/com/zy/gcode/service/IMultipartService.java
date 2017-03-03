package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;

/**
 * Created by admin5 on 17/3/2.
 */
public interface IMultipartService {
    CodeRe<String> getTappidByApp(String username, String appid);
}
