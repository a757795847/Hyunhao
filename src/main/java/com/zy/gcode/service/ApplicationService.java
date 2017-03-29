package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.ApplicationInfo;
import com.zy.gcode.service.intef.IApplicationService;
import com.zy.gcode.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by admin5 on 17/3/28.
 */
@Component
public class ApplicationService implements IApplicationService {
    @Autowired
    PersistenceService persistenceService;

    @Override
    @Transactional
    public List<ApplicationInfo> getApplications(Page page) {
        return  persistenceService.getList(ApplicationInfo.class,page);
    }

    @Override
    @Transactional
    public CodeRe add(ApplicationInfo applicationInfo) {
        return null;
    }

    @Override
    @Transactional
    public CodeRe update(ApplicationInfo applicationInfo) {
        return null;
    }
}
