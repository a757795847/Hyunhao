package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.service.intef.IPayService;
import com.zy.gcode.service.pay.RedTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @RequestMapping("upCatch")
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

    @RequestMapping("/uploadQR")
    public void begIndex(HttpServletRequest request,HttpServletResponse response){
        payService.setPayQR(response,request);
    }


    @RequestMapping("asyn")
    public DeferredResult<ModelAndView> get(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TimeUnit.SECONDS.sleep(3);
        return null;
    }
}
