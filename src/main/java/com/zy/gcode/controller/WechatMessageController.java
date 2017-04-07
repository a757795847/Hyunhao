package com.zy.gcode.controller;

import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.PayCredential;
import com.zy.gcode.pojo.WechatQrPay;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.*;
import com.zy.gcode.utils.wx.AesException;
import com.zy.gcode.utils.wx.WXBizMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin5 on 17/3/7.
 */
@Controller
public class WechatMessageController {

    static WXBizMsgCrypt wxBizMsgCrypt = null;

    static {
        try {
            wxBizMsgCrypt = new WXBizMsgCrypt(Constants.properties.getProperty("platform.token"),
                    Constants.properties.getProperty("platform.asekey"), Constants.properties.getProperty("platform.appid"));
        } catch (AesException e) {
            e.printStackTrace();
        }
    }
    @Autowired
    PersistenceService persistenceService;

    @RequestMapping(value = "/wechatPayMessage/handler")
    @Transactional
    public @ResponseBody String wechatPayMessage(@RequestBody String body){
        Du.pl("payMessage:"+body);
        Map<String,String> map = WxXmlParser.Xml2Map(body);
        PayCredential payCredential = persistenceService.get(PayCredential.class,(String)map.getOrDefault("appid",""));
        if (payCredential==null){
            return  "error";
        }
        Du.pl("map:"+map);
        Du.pl(UniqueStringGenerator.checkSignature(map,payCredential.getKey()));
        if(!UniqueStringGenerator.checkSignature(map,payCredential.getKey())){
            return "error";
        }
        WechatQrPay wechatQrPay = new WechatQrPay();
        wechatQrPay.setBankType(map.get("bank_type"));
        wechatQrPay.setFeeType(map.get("fee_type"));
        wechatQrPay.setMchId(map.get("mch_id"));
        wechatQrPay.setNonceStr(map.get("nonce_str"));
        wechatQrPay.setOpenid(map.get("openid"));
        wechatQrPay.setOutTradeNo("out_trade_no");
        wechatQrPay.setResultCode("result_code");
        wechatQrPay.setSign(map.get("sign"));
        wechatQrPay.setTransactionId(map.get("transaction_id"));
        try {
            long time = DateUtils.parse(map.getOrDefault("time_end","19700000145542"),"yyyyMMddHHmmss").getTime();
            wechatQrPay.setTimeEnd(new Timestamp(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        persistenceService.save(wechatQrPay);


        return "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }


    @RequestMapping(value = "/{appid}/receive", method = RequestMethod.GET)
    public
    @ResponseBody
    String index(@PathVariable String appid, String signature, String timestamp, String nonce, String echostr) {
        if (Constants.debug) {
            System.out.println("appid:" + appid);
            System.out.println("signature:" + signature);
            System.out.println("timestamp:" + timestamp);
            System.out.println("nonce:" + nonce);
            System.out.println("echostr:" + echostr);
        }
        String token = "ji0a4n2s6o7n";
        String[] strs = {token, timestamp, nonce};
        Arrays.sort(strs);
        String afterSha1 = UniqueStringGenerator.SHA1(MzUtils.merge(strs[0], strs[1], strs[2]));
        if (Constants.debug) {
            System.out.println("afterSha1:" + afterSha1);
        }
        if (afterSha1.equals(signature)) {
            return echostr;
        }
        return "";
    }

    @RequestMapping(value = "/{appid}/receive", method = RequestMethod.POST)
    public
    @ResponseBody
    String receiver(String msg_signature, String timestamp, String nonce,
                    @PathVariable String appid, @RequestBody String body) {
        System.out.println("appid:" + appid);
        try {
            System.out.println(wxBizMsgCrypt.decryptMsg(msg_signature, timestamp, nonce, body));
        } catch (AesException e) {
            e.printStackTrace();
            return null;
        }
        return "";
    }

}
