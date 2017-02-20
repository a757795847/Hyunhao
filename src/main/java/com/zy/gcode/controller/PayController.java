package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.pojo.User;
import com.zy.gcode.pojo.WxOperator;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.utils.Constants;
import org.apache.shiro.SecurityUtils;
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
    public @ResponseBody Object send(String openid,String count,String wxAppid,String sign) throws Exception{
        if(!sign.equals("13468794sagag")){
            Constants.objectMapper.writeValueAsString(ControllerStatus.error("签名错误"));
        }

       CodeRe<String> codeRe = payService.pay(openid,100,wxAppid);
       if(codeRe.isError()){
           return Constants.objectMapper.writeValueAsString(ControllerStatus.error(codeRe.getErrorMessage()));
       }
        return Constants.objectMapper.writeValueAsString(ControllerStatus.ok(codeRe.getMessage()));
    }

    @RequestMapping("redinfo")
    public @ResponseBody Object redinfo(String billno,String access_token,String zyid){
      CodeRe<RedStatus> redStatusCodeRe = payService.payInfo(billno,access_token,zyid);
      if (redStatusCodeRe.isError()){
        return redStatusCodeRe.getErrorMessage();
      }
      return  redStatusCodeRe.getMessage();
    }


}
