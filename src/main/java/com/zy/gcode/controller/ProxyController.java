package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.service.IProxyService;
import com.zy.gcode.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/2/27.
 */
@Controller
@RequestMapping("proxy")
public class  ProxyController{

    @Autowired
    IProxyService proxyService;

/*    @RequestMapping
    public  String home(){
        return null;
    }*/
    @RequestMapping("list")
    public @ResponseBody Object list(@RequestBody Map map){
        Page page = new Page();
        page.setCurrentPageIndex((int)map.get("currentPageIndex"));
        String zyappid = (String)map.get("zyappid");
        String isAuthentication = null;
        if(map.containsKey("isAuthentication")){
            isAuthentication = String.valueOf(map.get("isAuthentication"));
        }
        String serverType =null;
        if(map.containsKey("serverType")){
            serverType = String.valueOf(map.get("serverType"));
        }

       CodeRe<List> codeRe = proxyService.getZyAppInfo(serverType,isAuthentication,zyappid,page);
        return ControllerStatus.ok(codeRe.getMessage(),page);
    }

}
