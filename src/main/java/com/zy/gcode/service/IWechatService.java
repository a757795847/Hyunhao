package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.RedSubmitInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by admin5 on 17/2/14.
 */
public interface IWechatService {
    CodeRe sumbit(String image1,String image2,String image3,String billno,String openid);
}
