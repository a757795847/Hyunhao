package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.service.pay.RedTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/1/20.
 */
@Controller
@RequestMapping("pay")
public class PayController {

    @Autowired
    IPayService payService;


    @RequestMapping("index")
    public
    @ResponseBody
    String index() {
        return "welcome pay index!";
    }
    /**
     * 发送红包接口
     *
     * @param openid
     * @param count
     * @param tappid 数据库中生成的appid
     * @param sign
     * @return
     */
    @RequestMapping("send")
    public
    @ResponseBody
    Object send(String openid, String count, String tappid, String sign) {
        if (!sign.equals("13468794sagag")) {
            return ControllerStatus.error("签名错误");
        }

        CodeRe<String> codeRe = payService.pay(openid, 100, tappid);
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage());
    }

    /**
     * 获取红包详细信息
     *
     * @param billno
     * @param zyid
     * @return
     */
    @RequestMapping("redinfo")
    public
    @ResponseBody
    Object redinfo(String billno, String zyid) {
        CodeRe<RedStatus> redStatusCodeRe = payService.payInfo(billno, zyid);
        if (redStatusCodeRe.isError()) {
            return ControllerStatus.error(redStatusCodeRe.getErrorMessage());
        }
        Map map = new HashMap(2);
        map.put("redInfo", redStatusCodeRe.getMessage());
        return ControllerStatus.ok(map);
    }

    @RequestMapping("upCatch/")
    public
    @ResponseBody
    Object upCatch() {
        if (RedTimerTask.getInstance(payService::circularGetPayInfo, "upCatch").begin(7200)) {
            return ControllerStatus.ok();
        }
        return ControllerStatus.error("抓单已启动");
    }

    @RequestMapping("upReCatch")
    public
    @ResponseBody
    Object upReCatch() {
        if (RedTimerTask.getInstance(payService::pullIllegalBill, "upReCatch").begin(7200)) {
            return ControllerStatus.ok();
        }
        return ControllerStatus.error("抓单已启动");
    }

    public @ResponseBody  Map<User,String> get(){
        return null;
    }

    public static void main(String[] args) throws Exception{
       Method method = PayController.class.getMethod("get");
       ParameterizedType type =  (ParameterizedType)method.getGenericReturnType();
        System.out.println(method.getAnnotatedReturnType());
    }

}
