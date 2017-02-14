package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.RedSubmitInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by admin5 on 17/2/14.
 */
public interface IWechatService {
    CodeRe sumbit(MultipartFile[] multipartFiles,String billno,String openid);
}
