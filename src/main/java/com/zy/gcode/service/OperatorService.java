package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.intef.IOperatorService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.SubjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


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
        user.setName("u"+username);
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
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return CodeRe.error("重复的用户名");
        }
        user.setPassword(passwordService.encryptPassword(password));
        persistenceService.update(user);
        return CodeRe.correct("success");
    }

    @Override
    @Transactional
    public boolean passwordIsTrue(String oldPassword, String newPassword) {
        return passwordService.passwordsMatch(oldPassword, newPassword);
    }

    @Override
    @Transactional
    public User get(String userName) {
        return persistenceService.get(User.class,userName);
    }

    @Override
    public byte[] getUserHeadImage() throws IOException{
        File file = new File(Constants.USER_HEAD_IMAGE_PATH+"/"+SubjectUtils.getUserName());

        if(!file.exists()||file.length()==0){
            return null;
        }

        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;

    }

    @Override
    @Transactional
    public void uploadHeadImage(MultipartFile multipartFile) {
        User user = SubjectUtils.getUser();
        user.setHeadImage(Constants.USER_HEAD_IMAGE_PATH+"/"+SubjectUtils.getUserName());
        persistenceService.update(user);
        File file = new File(Constants.USER_HEAD_IMAGE_PATH+"/"+SubjectUtils.getUserName());
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
