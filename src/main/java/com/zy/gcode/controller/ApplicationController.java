package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.service.ApplicationService;
import com.zy.gcode.service.pay.OpenCondition;
import com.zy.gcode.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Object list(@RequestBody Map map) {
        Page page = new Page();
        page.setCurrentPageIndex(Optional.ofNullable((Integer) map.get(Page.CURRENTPAGEINDEX)).orElseGet(() -> 1));
        return ControllerStatus.ok(applicationService.getApplications(page));
    }

    @RequestMapping("info")
    public Object info(String id) {
        return ControllerStatus.ok(applicationService.info(id));
    }

    @RequestMapping("open")
    public Object open(@RequestBody Map map) {
        OpenCondition condition = new OpenCondition();
        if (map.containsKey(OpenCondition.COUNT)) {
            condition.setCount((int) map.get(OpenCondition.COUNT));
        }
        if (map.containsKey(OpenCondition.DAYCOUNT)) {
            condition.setCount((int) map.get(OpenCondition.DAYCOUNT));
        }
        if (!map.containsKey("id")) {
            return ControllerStatus.error("应用id不能为空");
        }
        CodeRe codeRe = applicationService.openApp((String) map.get("id"), condition);
        return codeRe.isError() ? ControllerStatus.error(codeRe.getErrorMessage()) : ControllerStatus.ok(codeRe.getMessage());
    }

    @RequestMapping("config/list/{appid}")
    public Object getConfigs(@PathVariable String appid) {
        CodeRe codeRe = applicationService.configList(appid);
        if (codeRe.isError()) {
            return ControllerStatus.error();
        }
        return ControllerStatus.ok(codeRe.getMessage());
    }

    @RequestMapping("close")
    public Object stopApp(String appid){
       CodeRe codeRe =  applicationService.closeApp(Long.parseLong(appid));
       if(codeRe.isError()){
           return ControllerStatus.error();
       }
       return ControllerStatus.ok();
    }

}
