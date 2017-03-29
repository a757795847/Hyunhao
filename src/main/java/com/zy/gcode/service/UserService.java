package com.zy.gcode.service;

import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.User;
import com.zy.gcode.pojo.ValidData;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.service.intef.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Transactional
    public String getState(String username) {
        return Optional.ofNullable(persistenceService.get(User.class,username)).orElseThrow(IllegalAccessError::new).getState();
    }

    @Override
    @Transactional
    public void setState(String username, String state) {
       User user =  getUser(username);
       if(user==null){
           throw new IllegalArgumentException();
       }
       user.setState(state);
       persistenceService.update(user);
    }
}
