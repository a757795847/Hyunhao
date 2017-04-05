package com.zy.gcode.service;

import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.User;
import com.zy.gcode.pojo.UserApp;
import com.zy.gcode.pojo.ValidData;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.service.intef.IUserService;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.SubjectUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by admin5 on 17/2/24.
 */
@Component
public class UserService implements IUserService {

    @Autowired
    PersistenceService persistenceService;

    @Override
    @Transactional
    public User getUser(String username) {
        return persistenceService.get(User.class, username);
    }

    @Override
    @Transactional
    public List<ValidData> getValidDateList(String username) {
        return persistenceService.getListByColumn(ValidData.class, "username", username);
    }

    @Override
    @Transactional
    public List<WechatPublicServer> getPublicServerList(String username) {
        return persistenceService.getListByColumn(WechatPublicServer.class, "userId", username);
    }

    @Override
    public void setUserAuthority(SimpleAuthorizationInfo authorityInfo) {
        List<UserApp> userApps = persistenceService.getListByColumn(UserApp.class, "userId", SubjectUtils.getUserName());
        if (userApps.isEmpty()) {
            return;
        }
        for (UserApp userApp : userApps) {
            if(isOpened(userApp)){
                authorityInfo.addStringPermission(userApp.getAppId());
            }
        }
    }

    @Override
    public void setRole(SimpleAuthorizationInfo authorizationInfo) {
        authorizationInfo.addRole(SubjectUtils.getUser().getRole());
    }

    public boolean isOpened(UserApp userApp) {
        if (userApp.getUseType() == UserApp.USE_BY_COUNT) {
            return userApp.getTotalCount() > userApp.getResidueCount();
        } else if (userApp.getUseType() == UserApp.USE_BY_TIME) {
            return isExpires(userApp.getEndTime());
        }
        return false;
    }

    private boolean isExpires(Timestamp after) {
        if (after == null) {
            return true;
        }
        return after.after(DateUtils.tNow());
    }
}
