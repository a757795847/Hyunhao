package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.User;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


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
    @Transactional
    public CodeRe checkUsername(String username) {
        User operator = persistenceService.get(User.class, username);
        if (operator == null) {
            return CodeRe.correct("success");
        }
        return CodeRe.error("user is exist");
    }


    @Override
    @Transactional
    public CodeRe registerOperator(String username, String password) {
        User existOperator = persistenceService.get(User.class, username);
        if (existOperator != null) {
            return CodeRe.error("用户名已存在!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordService.encryptPassword(password));
        user.setRole("user");
        persistenceService.save(user);
        return CodeRe.correct("success");
    }

    @Override
    public CodeRe generateVerificationCode(String phone) {
        return CodeRe.correct("1111");
    }

    @Override
    @Transactional
    public CodeRe updatePassword(String username, String password) {
        User user = persistenceService.get(User.class, username);
        if (user == null) {
            return CodeRe.error("重复的用户名");
        }
        user.setPassword(passwordService.encryptPassword(password));
        persistenceService.update(user);
        return CodeRe.correct("success");
    }
}
