package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.service.IRedStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/2/23.
 */
@Controller
@RequestMapping("redstrategy")
public class RedStrategycontroller {
    @Autowired
    IRedStrategyService strategyService;

    @RequestMapping("add")
    public @ResponseBody Object add(@RequestBody Map map){
        String name = (String)map.get("name");
        int money = (int)map.get("money");

       CodeRe codeRe =  strategyService.addStrategy(name,money,null);
       if(codeRe.isError()){
           return ControllerStatus.error(codeRe.getErrorMessage());
       }
       return ControllerStatus.ok(codeRe.getMessage().toString());

    }

    @RequestMapping("list")
    public @ResponseBody Object list(){
      CodeRe codeRe =  strategyService.listRedStrategy();
      if(codeRe.isError()){
          return  ControllerStatus.error(codeRe.getErrorMessage());
      }
      return ControllerStatus.ok((List)codeRe.getMessage());
    }

}
