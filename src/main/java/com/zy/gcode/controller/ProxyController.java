package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.IProxyService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.MzUtils;
import com.zy.gcode.utils.Page;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by admin5 on 17/2/27.
 */
@Controller
@RequestMapping("proxy")
public class ProxyController {

    @Autowired
    IProxyService proxyService;

    @RequestMapping("list")
    public
    @ResponseBody
    Object list(@RequestBody Map map) {
        Page page = new Page();
        page.setCurrentPageIndex((int) map.get("currentPageIndex"));
        String zyappid = (String) map.get("zyappid");
        String isAuthentication = null;
        if (map.containsKey("isAuthentication")) {
            isAuthentication = String.valueOf(map.get("isAuthentication"));
        }
        String serverType = null;
        if (map.containsKey("serverType")) {
            serverType = String.valueOf(map.get("serverType"));
        }

        CodeRe<List> codeRe = proxyService.getZyAppInfo(serverType, isAuthentication, zyappid, page);
        return ControllerStatus.ok(codeRe.getMessage(), page);
    }

    @RequestMapping("home")
    public String home() {
        return "/views/proxy/validityPeriod.html";
    }

    @RequestMapping("uploadPayQR")
    public
    @ResponseBody
    Object uploadPayQR(MultipartFile alipay, MultipartFile wechatPay, String count) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        try {
            if (alipay.isEmpty() || wechatPay.isEmpty()) {
                ControllerStatus.error("请上传微信和支付宝二维码");
            }
            alipay.transferTo(new File(MzUtils.merge(Constants.PAY_QR_PATH, "/", "alipay", user.getUsername())));
            wechatPay.transferTo(new File(MzUtils.merge(Constants.PAY_QR_PATH, "/", "wecpay", user.getUsername())));
            return ControllerStatus.ok("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return ControllerStatus.error("系统异常，上传失败!");
        }
    }

    @RequestMapping("QRUploadHome")
    public String QRUploadHome() {
        return "/views/publicNumber/upload.html";
    }

}
