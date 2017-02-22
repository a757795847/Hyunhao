package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataStrategy;
import com.zy.gcode.pojo.WxOperator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by admin5 on 17/2/22.
 */
@Component
public class OperatorService implements IOperatorService {

    @Autowired
    PersistenceService persistenceService;

    @Autowired
    PasswordService passwordService;

    @Override
    public CodeRe checkUsername(String username) {
       WxOperator operator = persistenceService.get(WxOperator.class,username);
        if(operator == null){
            return  CodeRe.correct("success");
        }
        return CodeRe.error("user is exist");
    }

    @Override
    public CodeRe topUp(String username, int count) {
        return null;
    }

    @Override
    public CodeRe registerOperator(String nick, String username, String password) {
        WxOperator existOperator = persistenceService.get(WxOperator.class,username);
        if(existOperator !=null){
          return CodeRe.error("用户名以存在!");
        }

        WxOperator wxOperator = new WxOperator();
        wxOperator.setUsername(username);
        wxOperator.setName(nick);
        wxOperator.setPassword(passwordService.encryptPassword(password));
        persistenceService.save(wxOperator);
        return CodeRe.correct("success");
    }

}
