package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.BatchRe;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.RedStatus;

/**
 * Created by admin5 on 17/1/20.
 */
public interface IPayService {
    CodeRe pay(String id, int count, String tappid);

    CodeRe<RedStatus> payInfo(String billno, String geappid);

    BatchRe circularGetPayInfo();

    BatchRe pullIllegalBill();
}
