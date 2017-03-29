package com.zy.gcode.service.intef;

import com.zy.gcode.pojo.User;
import com.zy.gcode.pojo.ValidData;
import com.zy.gcode.pojo.WechatPublicServer;

import java.util.List;

/**
 * Created by admin5 on 17/2/24.
 */
public interface IUserService {
    User getUser(String username);

    List<ValidData> getValidDateList(String username);

    List<WechatPublicServer> getPublicServerList(String username);

    String getState(String username);

    void setState(String username,String state);
}