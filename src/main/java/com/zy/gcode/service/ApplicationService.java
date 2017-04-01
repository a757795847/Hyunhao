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

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
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
    @Transactional
    public CodeRe openApp(String appid, OpenCondition openCondition) {
        ApplicationInfo applicationInfo = persistenceService.get(ApplicationInfo.class, appid);

        if (applicationInfo == null) {
            return CodeRe.error("该app不存在");
        }
        if (!canOpen(applicationInfo)) {
            return CodeRe.error("您暂时无权开通,请阅读开通前提");
        }

        if (isOpened(applicationInfo)) {
            return CodeRe.error("该应用已经开通");
        }
        UserApp userApp = new UserApp();
        switch (applicationInfo.getPayCdn()) {
            case ApplicationInfo.PAY_FREE_TO_USE:
                userApp.setBegTime(DateUtils.tNow());
                userApp.setUseType(UserApp.USE_BY_TIME);
                userApp.setUserId(SubjectUtils.getUserName());
                userApp.setAppId(appid);
                break;
            case ApplicationInfo.PAY_BY_COUNT:
                userApp.setBegTime(DateUtils.tNow());
                userApp.setUseType(UserApp.USE_BY_COUNT);
                userApp.setTotalCount(openCondition.getCount());
                userApp.setResidueCount(openCondition.getCount());
                userApp.setUserId(SubjectUtils.getUserName());
                userApp.setAppId(appid);
                break;
            case ApplicationInfo.PAY_BY_DAY:
                userApp.setBegTime(DateUtils.tNow());
                userApp.setUseType(UserApp.USE_BY_TIME);
                userApp.setEndTime(DateUtils.afterOfDay(openCondition.getDayCount()));
                userApp.setUserId(SubjectUtils.getUserName());
                userApp.setAppId(appid);
                break;
            case ApplicationInfo.PAY_BY_RATE:
                userApp.setBegTime(DateUtils.tNow());
                userApp.setUseType(UserApp.USE_BY_TIME);
                userApp.setUserId(SubjectUtils.getUserName());
                userApp.setAppId(appid);
                break;
            default:
                throw new IllegalStateException("该类型[" + applicationInfo.getPayCdn() + "]不存在!请检查数据有效性");
        }
        persistenceService.save(userApp);
        return CodeRe.correct("开通成功");
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


    private List<UserApp> getOpenApp() {
        List<UserApp> userAppList = persistenceService.getListByColumn(UserApp.class, "userId", SubjectUtils.getUserName());
        List<UserApp> openedAppList = new ArrayList<>();
        for (UserApp userApp : userAppList) {
            if (userApp.getUseType() == UserApp.USE_BY_COUNT) {
                if (userApp.getTotalCount() > userApp.getResidueCount()) {
                    openedAppList.add(userApp);
                }
            } else if (userApp.getUseType() == UserApp.USE_BY_TIME) {
                if (isExpires(userApp.getEndTime())) {
                    openedAppList.add(userApp);
                }
            }
        }
        return openedAppList;

    }


    public boolean canOpen(ApplicationInfo info) {
        switch (info.getOpenCdn()) {
            case ApplicationInfo.OPEN_BY_ANY:
                return true;
            case ApplicationInfo.OPEN_BY_APP:
                String option = info.getOpenOption();
                if(option==null||option.length()==0){
                    return true;
                }
                List<UserApp> userAppList = getOpenApp();
                if(userAppList.isEmpty()){
                    return false;
                }
                String[] options = option.split(",");
                for (UserApp userApp : userAppList) {
                    boolean flag = true;
                    for (String needId : options) {
                        long id = Long.parseLong(needId);
                        if (id == userApp.getId()) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        return false;
                    }
                }

                return true;
            default:
                throw new IllegalStateException("该类型[" + info.getOpenCdn() + "]不存在!请检查数据有效性");
        }

    }

    @Override
    public void setAppQR(String id, HttpServletResponse response) {

    }
}
