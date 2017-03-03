package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.RedStatus;

/**
 * Created by admin5 on 17/1/20.
 */
public interface IPayService {
    CodeRe pay(String id, int count, String geappid);

    CodeRe<RedStatus> payInfo(String billno, String token, String geappid);

    CodeRe circularGetPayInfo();
}
