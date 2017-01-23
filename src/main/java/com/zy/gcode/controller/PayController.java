package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public @ResponseBody Object send(){
        return payService.pay("ooBfdwNcoMaol2CF0zlcRUYkYE_Q",100).getMessage();
    }

    @RequestMapping("redinfo")
    public @ResponseBody Object redinfo(String billno){
      CodeRe<RedStatus> redStatusCodeRe = payService.payInfo(billno);
      if (redStatusCodeRe.isError()){
        return redStatusCodeRe.getErrorMessage();
      }
      return  redStatusCodeRe.getMessage();
    }


}
