package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Properties;

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
    public @ResponseBody Object send(@RequestParam String phone, HttpSession session){
        User user = (User)session.getAttribute("c_user");
        Properties properties = new Properties();
        try {
            properties.load(new ClassPathResource("config.properties").getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(phone.equals(properties.getProperty("phone")))
        return payService.pay(user.getOpenId(),1).getMessage();
        return "false";
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
