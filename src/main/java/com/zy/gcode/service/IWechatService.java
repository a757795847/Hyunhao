package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;

/**
 * Created by admin5 on 17/2/14.
 */
public interface IWechatService {
    CodeRe sumbit(String image1,String image2,String image3,String billno,String openid,String appid);
}
