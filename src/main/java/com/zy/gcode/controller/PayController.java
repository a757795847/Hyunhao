package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by admin5 on 17/1/20.
 */
@Controller
@RequestMapping("pay")
public class PayController {
    @Autowired
    IPayService payService;

    @RequestMapping("index")
    public @ResponseBody String index(){
        return "welcome pay index!";
    }
    @RequestMapping("send")
    public @ResponseBody Object send(String appid,String openid,String count,String access_token,String zyid){
       CodeRe<String> codeRe = payService.pay(openid,Integer.parseInt(count),appid,access_token,zyid);
       if(codeRe.isError()){
           return ControllerStatus.error(codeRe.getErrorMessage());
       }
        return ControllerStatus.ok(codeRe.getMessage());
    }

    @RequestMapping("redinfo")
    public @ResponseBody Object redinfo(String billno,String appid,String access_token,String zyid){
      CodeRe<RedStatus> redStatusCodeRe = payService.payInfo(billno,appid,access_token,zyid);
      if (redStatusCodeRe.isError()){
        return redStatusCodeRe.getErrorMessage();
      }
      return  redStatusCodeRe.getMessage();
    }


}
