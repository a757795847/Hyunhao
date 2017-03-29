package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.ApplicationInfo;
import com.zy.gcode.service.intef.IApplicationService;
import com.zy.gcode.utils.Page;

import java.util.List;

/**
 * Created by admin5 on 17/3/28.
 */
public class ApplicationService implements IApplicationService {

    @Override
    public List<ApplicationInfo> getApplications(Page page) {
        return null;
    }

    @Override
    public CodeRe add(ApplicationInfo applicationInfo) {
        return null;
    }

    @Override
    public CodeRe update(ApplicationInfo applicationInfo) {
        return null;
    }
}
