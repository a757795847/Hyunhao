package com.zy.gcode.service.intef;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.ApplicationInfo;
import com.zy.gcode.utils.Page;

import java.util.List;

/**
 * Created by admin5 on 17/3/28.
 */
public interface IApplicationService{
    List<ApplicationInfo> getApplications(Page page);
    CodeRe add(ApplicationInfo applicationInfo);
    CodeRe update(ApplicationInfo applicationInfo);
    ApplicationInfo info(String appid);
}
