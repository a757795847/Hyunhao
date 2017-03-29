package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.ApplicationInfo;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.service.intef.IApplicationService;
import com.zy.gcode.utils.Page;
import com.zy.gcode.utils.SubjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/3/28.
 */
@Component
public class ApplicationService implements IApplicationService {
    @Autowired
    PersistenceService persistenceService;

    @Override
    @Transactional
    public List<Map> getApplications(Page page) {
        List<ApplicationInfo> applicationInfos = persistenceService.getList(ApplicationInfo.class, page);
        List<WechatPublicServer> wechatPublicServers = persistenceService.getListByColumn(WechatPublicServer.class, "createUserId", SubjectUtils.getUserName());
        List<Map> results = new ArrayList<>();
        for (ApplicationInfo app : applicationInfos) {
            Map map = new HashMap();
            map.put("name", app.getName());
            map.put("applicationInfo", app.getApplicationInfo());
            map.put("isOpened", false);
            map.put("id",app.getId());
            map.put("backgroundColor",app.getBackgroundColor());
            for (WechatPublicServer server : wechatPublicServers) {
                if (server.getZyappid().equals(app.getId()) && server.getIsAuthentication().equals("1")) {
                    map.put("isOpened", true);
                    break;
                }
            }
            results.add(map);
        }
        return results;
    }

    @Override
    @Transactional
    public Map info(String appid) {
        Map map = new HashMap(2,1.0f);
     ApplicationInfo applicationInfo =   persistenceService.get(ApplicationInfo.class, appid);
     map.put("applicationInfo",applicationInfo);
     map.put("isOpened",false);
     if(applicationInfo!=null){
         WechatPublicServer wechatPublicServer = persistenceService.
                 getOneByColumn(WechatPublicServer.class,"zyappid",applicationInfo.getId(),"createUserId",SubjectUtils.getUserName());
         if(wechatPublicServer!=null){
             map.put("isOpened",wechatPublicServer.getIsAuthentication().equals("1")?true:false);
         }
     }
     return map;

    }

    @Override
    @Transactional
    public CodeRe add(ApplicationInfo applicationInfo) {
        return null;
    }

    @Override
    @Transactional
    public CodeRe update(ApplicationInfo applicationInfo) {
        return null;
    }
}
