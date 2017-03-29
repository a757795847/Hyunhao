package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.service.intef.IMultipartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用于处理一个用户绑定多个微信号之间的关系
 * Created by admin5 on 17/3/2.
 */
@Service
public class MultipartService implements IMultipartService {

    @Autowired
    PersistenceService persistenceService;

    /**
     * 通过用户名和追游appid获取对应的微信appid
     *
     * @param username
     * @param appid
     * @return
     */

    @Override
    public CodeRe<String> getTappidByApp(String username, String appid) {
        List<WechatPublicServer> wechatPublicServers = persistenceService.getListByColumn(WechatPublicServer.class, "userId", username, "zyappid", appid);
        if (wechatPublicServers.isEmpty()) {
            return CodeRe.error("不存在的服务");
        }

        return CodeRe.correct(wechatPublicServers.get(0).getTappid());
    }
}
