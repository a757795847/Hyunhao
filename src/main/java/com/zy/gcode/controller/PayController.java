package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.BatchRe;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin5 on 17/1/20.
 */
@Controller
@RequestMapping("pay")
public class PayController {

    @Autowired
    IPayService payService;


    RedInfoTimerTask redInfoTimerTask = new RedInfoTimerTask();
    UpdateRedInfoTimerTask updateRedInfoTimerTask = new UpdateRedInfoTimerTask();

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
     * @param geAppid 数据库中生成的appid
     * @param sign
     * @return
     */
    @RequestMapping("send")
    public
    @ResponseBody
    Object send(String openid, String count, String geAppid, String sign){
        if (!sign.equals("13468794sagag")) {
            return ControllerStatus.error("签名错误");
        }

        CodeRe<String> codeRe = payService.pay(openid, 100, geAppid);
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
        if (redInfoTimerTask.begin(3600)) {
            return ControllerStatus.ok();
        }
        return ControllerStatus.error("抓单已启动");
    }

    @RequestMapping("upReCatch")
    public
    @ResponseBody
    Object upReCatch() {
        if (updateRedInfoTimerTask.begin(3600)) {
            return ControllerStatus.ok();
        }
        return ControllerStatus.error("抓单已启动");
    }

    /**
     * 用于执行抓取红包状态
     */

    private class RedInfoTimerTask extends TimerTask {
        Timer timer;
        boolean isStop = true;

        private RedInfoTimerTask() {
        }

        public boolean begin(int seconds) {
            if (!isStop) {
                return false;
            }
            timer = new Timer();
            timer.schedule(this, 5000, seconds * 1000);
            isStop = false;
            return true;
        }

        @Override
        public void run() {
            BatchRe batchRe = (BatchRe) payService.circularGetPayInfo();
            if (Constants.debug) {
                System.out.println("ok:" + batchRe.getMessage());
                System.out.println("error:" + batchRe.getErrorList());
            }
            if (batchRe.getMessage().isEmpty()) {
                cancel();
                timer.cancel();
                isStop = true;
                System.out.println("系统抓取订单已结束调用");
            }
        }
    }

    /**
     * 用于更新红包状态
     */
    private class UpdateRedInfoTimerTask extends TimerTask {
        Timer timer;

        private UpdateRedInfoTimerTask() {
        }

        public boolean begin(int seconds) {
            if (timer != null) {
                return false;
            }
            timer = new Timer();
            timer.schedule(this, 5000, seconds * 1000);
            return true;
        }


        @Override
        public void run() {
            BatchRe batchRe = (BatchRe) payService.pullIllegalBill();
            if (Constants.debug) {
                System.out.println("ok:" + batchRe.getMessage());
                System.out.println("error:" + batchRe.getErrorList());
            }
        }
    }


}
