package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.pojo.WechatUserInfo;
import com.zy.gcode.service.intef.IAuthenticationService;
import com.zy.gcode.service.intef.IWechatService;
import com.zy.gcode.utils.JsonUtils;
import com.zy.gcode.utils.wx.JsapiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Created by admin5 on 17/2/14.
 */
@RequestMapping("wechat/view")
@Controller
public class WechatController {
    @Autowired
    IAuthenticationService authenticationService;

    @Autowired
    IWechatService wechatService;

    @RequestMapping("home/{tAppid}")
    public String home(@PathVariable("tAppid") String tAppid, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        WechatUserInfo wechatUserInfo = (WechatUserInfo) session.getAttribute("c_user");
        WechatPublicServer wechatPublicServer = wechatService.getWechatPublic(tAppid);

        if (wechatUserInfo != null) {
            Map<String, String> map = JsapiUtils.sign(authenticationService.getJsapiTicketByAppid(wechatPublicServer.getWxAppid()).getMessage(),
                    request.getRequestURL().toString());
            map.put("appid", wechatPublicServer.getWxAppid());
            request.setAttribute("jsonConfig", JsonUtils.objAsString(map));
            return "/views/wechat/submit.html";
        }
        return null;
    }
    @RequestMapping("submit")
    @ResponseBody
    public Map submit(@RequestParam(required = false) String image1, @RequestParam(required = false) String image2,
                      @RequestParam(required = false) String image3, @RequestParam String billno, HttpSession session) throws IOException {
        WechatUserInfo wechatUserInfo = (WechatUserInfo) session.getAttribute("c_user");
        if (wechatUserInfo == null) {
            return ControllerStatus.error("登录过期");
        }
        CodeRe<String> codeRe = wechatService.sumbit(image1, image2, image3, billno, wechatUserInfo.getOpenId(), wechatUserInfo.getAppid(),wechatUserInfo.getNick());
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage());
    }


}
