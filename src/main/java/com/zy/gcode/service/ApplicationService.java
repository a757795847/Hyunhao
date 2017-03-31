package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.ApplicationInfo;
import com.zy.gcode.pojo.UserApp;
import com.zy.gcode.service.intef.IApplicationService;
import com.zy.gcode.service.pay.OpenCondition;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.Page;
import com.zy.gcode.utils.SubjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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
        List<Map> results = new ArrayList<>();
        for (ApplicationInfo app : applicationInfos) {
            Map map = new HashMap();
            map.put("name", app.getName());
            map.put("applicationInfo", app.getApplicationInfo());
            map.put("isOpened", isOpened(app));
            map.put("id", app.getId());
            map.put("backgroundColor", app.getBackgroundColor());

            results.add(map);
        }
        return results;
    }

    public boolean isOpened(ApplicationInfo info) {
        List<UserApp> userApps = persistenceService.getListByColumn(UserApp.class, "userId", SubjectUtils.getUserName(), "appId", info.getId());
        for (UserApp userApp : userApps) {
            if (userApp.getUseType() == UserApp.USE_BY_COUNT) {
                return userApp.getTotalCount() > userApp.getResidueCount();
            } else if (userApp.getUseType() == UserApp.USE_BY_TIME) {
                return isExpires(userApp.getEndTime());
            }
            return false;
        }
        return false;
    }

    private boolean isExpires(Timestamp after) {
        if (after == null) {
            return true;
        }
        return after.after(DateUtils.tNow());
    }


    @Override
    @Transactional
    public Map info(String appid) {
        Map map = new HashMap(2, 1.0f);
        ApplicationInfo applicationInfo = persistenceService.get(ApplicationInfo.class, appid);
        map.put("applicationInfo", applicationInfo);
        map.put("isOpened", isOpened(applicationInfo));
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

    @Override
    public CodeRe openApp(String appid, OpenCondition openCondition) {
        ApplicationInfo applicationInfo = persistenceService.get(ApplicationInfo.class, appid);
        if (applicationInfo == null) {
            return CodeRe.error("该app不存在");
        }
        if(isOpened(applicationInfo)){
            return CodeRe.error("该应用已经开通");
        }
        UserApp userApp = new UserApp();
        switch (applicationInfo.getPayCdn()) {
            case ApplicationInfo.PAY_FREE_TO_USE:
                userApp.setBegTime(DateUtils.tNow());
                userApp.setUseType(UserApp.USE_BY_TIME);
                break;
            case ApplicationInfo.PAY_BY_COUNT:
                userApp.setBegTime(DateUtils.tNow());
                userApp.setUseType(UserApp.USE_BY_COUNT);
                userApp.setTotalCount(openCondition.getCount());
                userApp.setResidueCount(openCondition.getCount());
                break;
            case ApplicationInfo.PAY_BY_DAY:
                userApp.setBegTime(DateUtils.tNow());
                userApp.setUseType(UserApp.USE_BY_TIME);
                userApp.setEndTime(DateUtils.afterOfDay(openCondition.getDayCount()));
                break;
            case ApplicationInfo.PAY_BY_RATE:
                userApp.setBegTime(DateUtils.tNow());
                userApp.setUseType(UserApp.USE_BY_TIME);
                break;
        }
        persistenceService.save(userApp);


        return CodeRe.correct("开通成功");
    }

}
