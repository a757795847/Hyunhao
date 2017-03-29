package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.service.ApplicationService;
import com.zy.gcode.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * Created by admin5 on 17/3/29.
 */
@RestController
@RequestMapping("app")
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;

    @RequestMapping("list")
    public Object appInfo(@RequestBody Map map) {
        Page page = new Page();
        page.setCurrentPageIndex(Optional.ofNullable((Integer) map.get(Page.CURRENTPAGEINDEX)).orElseGet(() -> 1));
        return ControllerStatus.ok(applicationService.getApplications(page));
    }
}
