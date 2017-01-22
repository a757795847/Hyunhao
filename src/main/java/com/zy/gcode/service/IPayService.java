package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;

/**
 * Created by admin5 on 17/1/20.
 */
public interface IPayService {
    CodeRe pay(String openid,int count);
    CodeRe payInfo(String billno);
}
