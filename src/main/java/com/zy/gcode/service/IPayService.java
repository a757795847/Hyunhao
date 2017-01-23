package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.RedStatus;

/**
 * Created by admin5 on 17/1/20.
 */
public interface IPayService {
    CodeRe pay(String openid,int count);
    CodeRe<RedStatus> payInfo(String billno);
}
