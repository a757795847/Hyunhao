package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.ApplicationInfo;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/2/27.
 */
@Service
public class ProxyService implements IProxyService {

    @Autowired
    PersistenceService persistenceService;


    @Override
    @Transactional
    public CodeRe getZyAppInfo(String serverType, String isAuthentication, String zyappid, Page page) {

        List<WechatPublicServer> wechatPublicServerList = persistenceService.getListByColumn(WechatPublicServer.class, page, "isAuthentication",
                isAuthentication, "zyappid", zyappid);
        ArrayList results = new ArrayList();
        boolean isServered = false;
        boolean isExpires = false;
        if(serverType!=null){
            if(serverType.equals("1")){
                isServered = true;
            }else if(serverType.equals("2")){
                isExpires = true;
            }
        }

        for (WechatPublicServer publicServer : wechatPublicServerList) {
            Map map;
            if(publicServer.getEndTime().getTime()<System.currentTimeMillis()){
                if(isServered){
                    page.setCount(page.getCount()-1);
                    continue;
                }
                map = new LinkedHashMap();
                map.put("serverType", "已过期");

            }else{
                if(isExpires){
                    page.setCount(page.getCount()-1);
                    continue;
                }
                map = new LinkedHashMap();
                map.put("serverType", "服务中");
            }
            map.put("beginTime", publicServer.getBeginTime());
            map.put("endTime", publicServer.getEndTime());
            map.put("serverId", publicServer.getTappid());
            map.put("phone", publicServer.getUserId());
            map.put("publicName", publicServer.getWxName());
            map.put("isAuthentication", publicServer.getIsAuthentication());
            ApplicationInfo applicationInfo =  persistenceService.get(ApplicationInfo.class,publicServer.getZyappid());
            map.put("zyappName",applicationInfo.getName());
            results.add(map);
        }
        return CodeRe.correct(results);
    }

}
