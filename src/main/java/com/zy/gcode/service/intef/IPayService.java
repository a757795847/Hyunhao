package com.zy.gcode.service.intef;

import com.zy.gcode.controller.delegate.BatchRe;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.RedStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by admin5 on 17/1/20.
 */
public interface IPayService {
    CodeRe pay(String id, int count, String tappid);

    CodeRe<RedStatus> payInfo(String billno, String geappid);

    BatchRe circularGetPayInfo();

    BatchRe pullIllegalBill();

    CodeRe getWechatPayUrl(int count,String remoteAddr);
     CodeRe dealPayRecord(Map<String,String> map);
}
