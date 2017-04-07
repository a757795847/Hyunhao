package com.zy.gcode.service.intef;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by admin5 on 17/2/17.
 */
public interface IOperatorService {
    CodeRe registerOperator(String username, String password);

    CodeRe checkUsername(String username);

    CodeRe generateVerificationCode(String phone);

    CodeRe updatePassword(String username, String password);

    boolean passwordIsTrue(String oldPassword, String newPassword);

    User get(String userName);

    byte[] getUserHeadImage() throws IOException;

    void uploadHeadImage(MultipartFile multipartFile);

}
