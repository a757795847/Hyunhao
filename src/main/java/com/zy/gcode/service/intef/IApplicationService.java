package com.zy.gcode.service.intef;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.ApplicationInfo;
import com.zy.gcode.utils.Page;
import org.aspectj.apache.bcel.classfile.Code;

import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/3/28.
 */
public interface IApplicationService{
    List<Map> getApplications(Page page);
    CodeRe add(ApplicationInfo applicationInfo);
    CodeRe update(ApplicationInfo applicationInfo);
    Map info(String appid);
    CodeRe openApp(String appid);
}
