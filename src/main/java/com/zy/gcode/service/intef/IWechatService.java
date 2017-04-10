package com.zy.gcode.service.intef;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.UserConfig;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.pojo.WechatUserInfo;

import java.io.Serializable;

/**
 * Created by admin5 on 17/2/14.
 */
public interface IWechatService {
    CodeRe sumbit(String image1, String image2, String image3, String billno, String openid, String appid, String nick, String tAppid);

    WechatPublicServer getWechatPublic(String id);

    WechatUserInfo getUser(String openid);

    CodeRe<UserConfig> getUserConfig(Serializable id);
}
