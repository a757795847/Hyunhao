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
     * @param geAppid 数据库中生成的appid
     * @param sign
     * @return
     * @throws Exception
     */
    @RequestMapping("send")
    public
    @ResponseBody
    Object send(String openid, String count, String geAppid, String sign) throws Exception {
        if (!sign.equals("13468794sagag")) {
            return Constants.objectMapper.writeValueAsString(ControllerStatus.error("签名错误"));
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
     * @param access_token
     * @param zyid
     * @return
     */
    @RequestMapping("redinfo")
    public
    @ResponseBody
    Object redinfo(String billno, String access_token, String zyid) {
        CodeRe<RedStatus> redStatusCodeRe = payService.payInfo(billno, access_token, zyid);
        if (redStatusCodeRe.isError()) {
            return redStatusCodeRe.getErrorMessage();
        }
        return redStatusCodeRe.getMessage();
    }

    @RequestMapping("upCatch")
    public @ResponseBody Object upCatch(){
       BatchRe batchRe = (BatchRe) payService.circularGetPayInfo();
        return ControllerStatus.ok(batchRe.getMessage());
    }


}
