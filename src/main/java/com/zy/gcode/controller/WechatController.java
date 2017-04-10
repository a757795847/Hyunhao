package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.UserConfig;
import com.zy.gcode.pojo.WechatUserInfo;
import com.zy.gcode.service.intef.IAuthenticationService;
import com.zy.gcode.service.intef.IWechatService;
import com.zy.gcode.utils.JsonUtils;
import com.zy.gcode.utils.TappidUtils;
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
        TappidUtils.TappidEntry tappidEntry = TappidUtils.deTappid(tAppid);
        CodeRe<UserConfig> codeRe = wechatService.getUserConfig(tappidEntry.getUserConfigId());
        if (codeRe.isError()) {
            return null;
        }
        UserConfig userConfig = codeRe.getMessage();
        if (wechatUserInfo != null) {
            Map<String, String> map = JsapiUtils.sign(authenticationService.getJsapiTicketByAppid(userConfig.getWechatOfficialId()).getMessage(),
                    request.getRequestURL().toString());
            map.put("appid", userConfig.getWechatOfficialId());
            request.setAttribute("jsonConfig", JsonUtils.objAsString(map));
            return "/views/wechat/submit.html";
        }
        return null;
    }

    @RequestMapping("submit/{tAppid}")
    @ResponseBody
    public Map submit(@RequestParam(required = false) String image1, @RequestParam(required = false) String image2,
                      @RequestParam(required = false) String image3, @RequestParam String billno, HttpSession session, @PathVariable String tAppid) throws IOException {
        WechatUserInfo wechatUserInfo = (WechatUserInfo) session.getAttribute("c_user");
        if (wechatUserInfo == null) {
            return ControllerStatus.error("登录过期");
        }
        CodeRe<String> codeRe = wechatService.sumbit(image1, image2, image3, billno, wechatUserInfo.getOpenId(), wechatUserInfo.getAppid(), wechatUserInfo.getNick(), tAppid);
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage());
    }


}
