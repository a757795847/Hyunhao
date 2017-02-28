package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.WechatPublic;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin5 on 17/2/27.
 */
@Service
public class ProxyService implements IProxyService{

    @Autowired
    PersistenceService persistenceService;

    @Override
    public CodeRe getZyAppInfo(String serverType, String isAuthentication, String zyappid, Page page) {
        persistenceService.getList(WechatPublicServer.class,page);
        return null;
    }
}
