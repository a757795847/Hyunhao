package com.zy.gcode.controller;

import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.MzUtils;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

/**
 * Created by admin5 on 17/3/7.
 */
@Controller
@RequestMapping("wcMs")
public class WechatMessageController {
    @RequestMapping
    public
    @ResponseBody
    String index(String signature,String timestamp,String nonce,String echostr) {
        if(Constants.debug){
            System.out.println("signature:"+signature);
            System.out.println("timestamp:"+timestamp);
            System.out.println("nonce:"+nonce);
            System.out.println("echostr:"+echostr);
        }
        String token = "86868xyz";
        String[] strs = {token,timestamp,nonce};
        Arrays.sort(strs);
        String afterSha1 = UniqueStringGenerator.SHA1(MzUtils.merge(strs[0],strs[1],strs[2]));
        if(Constants.debug){
            System.out.println("afterSha1:"+afterSha1);
        }
        if(afterSha1.equals(signature)){
            return  echostr;
        }
        return "";
    }

}
