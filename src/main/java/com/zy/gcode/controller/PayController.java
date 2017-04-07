package com.zy.gcode.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.service.intef.IPayService;
import com.zy.gcode.service.pay.RedTimerTask;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @RequestMapping("/qr")
    @ResponseBody
    public Object begIndex(HttpServletRequest request, @RequestBody Map map){
      CodeRe codeRe = payService.getWechatPayUrl((int)map.getOrDefault("count",10000),request.getRemoteAddr());
      if(codeRe.isError()){
          return ControllerStatus.error();
      }
      return ControllerStatus.ok(codeRe.getMessage());
    }


    @RequestMapping("asyn")
    public DeferredResult<ModelAndView> get(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TimeUnit.SECONDS.sleep(3);
        return null;
    }
    @RequestMapping("test")
    public void qr(String value,HttpServletResponse response){
        response.setHeader(HttpHeaders.CACHE_CONTROL,"no-cache");
        response.setHeader(HttpHeaders.CONTENT_TYPE,"image/png");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(value,BarcodeFormat.QR_CODE,200,200);
            MatrixToImageWriter.writeToStream(bitMatrix,"png",response.getOutputStream());
        } catch (WriterException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
