package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.RedSubmitInfo;
import com.zy.gcode.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by admin5 on 17/2/14.
 */
@Component
public class WechatService implements IWechatService {
    @Autowired
    PersistenceService persistenceService;


    @Override
    public CodeRe sumbit(MultipartFile[] multipartFiles, String billno,String openid) {
        String path = new StringBuilder(Constants.RED_PICTURE_PATH).append("/").append(billno).toString();
        RedSubmitInfo redSubmitInfo = new RedSubmitInfo();
        try {
            switch (multipartFiles.length){
                case 1:
                    String a = path+"A";
                    multipartFiles[1].transferTo(new File(a));
                    redSubmitInfo.setPic1Path(path+"A");
                    break;
                case 2:
                    String a1 = path+"A";
                    multipartFiles[1].transferTo(new File(a1));
                    String b = path+"B";
                    multipartFiles[1].transferTo(new File(b));
                    redSubmitInfo.setPic1Path(path+"A");
                    redSubmitInfo.setPic1Path(path+"B");
                    break;
                case 3:
                    String a3 = path+"A";
                    multipartFiles[1].transferTo(new File(a3));
                    String b2 = path+"B";
                    multipartFiles[1].transferTo(new File(b2));
                    String c = path+"C";
                    multipartFiles[1].transferTo(new File(c));
                    redSubmitInfo.setPic1Path(path+"A");
                    redSubmitInfo.setPic1Path(path+"B");
                    redSubmitInfo.setPic1Path(path+"C");
                    break;
                default:
                    CodeRe.error("最多只支持上传4张图片");
                    break;
            }
        } catch (IOException e) {
            return CodeRe.error("文件写入错误! 请联系管理员");
        }
        redSubmitInfo.setOpenId(openid);
        redSubmitInfo.setRsNumber(billno);
        persistenceService.save(redSubmitInfo);

        return CodeRe.correct("success");
    }

}
