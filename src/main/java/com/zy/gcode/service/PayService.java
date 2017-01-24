package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.RedBill;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.service.pay.RedPayInfo;
import com.zy.gcode.service.pay.RedReqInfo;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by admin5 on 17/1/20.
 */
@Service
public class PayService implements IPayService {

    @Autowired
    PersistenceService persistenceService;

    String[] strs = {"生活如意，事业高升","前程似锦，美梦成真","年年今日，岁岁今朝","百事可乐，万事芬达","愿与同僚，共分此乐","事业有成，幸福快乐","幸福快乐，与君同在","生日快乐，幸福安康"};

    @Override
    public CodeRe pay(String openid, int count) {
        RedPayInfo payInfo = new RedPayInfo();
        payInfo.setNonce_str(UniqueStringGenerator.getUniqueCode());
        payInfo.setMch_billno(UniqueStringGenerator.wxbillno());
        payInfo.setMch_id(UniqueStringGenerator.MCH_ID);
        payInfo.setWxappid("wx653d39223641bea7");
        payInfo.setSend_name("追游科技");
        payInfo.setRe_openid(openid);
        payInfo.setTotal_num(1);
        payInfo.setTotal_amount((int)(Math.random()*10));
        payInfo.setWishing(strs[(int)(Math.random()*8)]);
        payInfo.setClient_ip("115.29.188.190");
        payInfo.setAct_name("好评返现");
        payInfo.setRemark("多来多得");
        Map<String,String> map = WxXmlParser.getWxMap(payInfo);
        Set<String> keys = map.keySet();
        Object[] objs =  keys.toArray();
        Arrays.sort(objs);
        StringBuilder builder = new StringBuilder();
        for(int i = 0 ;i <objs.length;i++){
            builder.append(objs[i].toString()).append("=")
                    .append(map.get(objs[i])).append("&");
        }
        builder.append("key=acjkgkliutguizkjgailzsghqyesiu11");
        payInfo.setSign(UniqueStringGenerator.getMd5(builder.toString()));
        HttpResponse response = HttpClientUtils.SSLPostSend("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack",WxXmlParser.getWxXml(payInfo));
        StringBuilder builder1 = new StringBuilder();
        try(BufferedReader reader =new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"))){
            String line;
            while((line = reader.readLine())!=null){
                builder1.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
       Map<String,String> reMap = WxXmlParser.Xml2Map(builder1.toString());

        if(!reMap.containsKey("return_code")){
          return CodeRe.error("failure request");

        }
        String recode = reMap.get("return_code");
        if(!recode.equals("SUCCESS")){
            if(reMap.containsKey("return_msg")){
                return  CodeRe.error(reMap.get("return_msg"));
            }
            return CodeRe.error("未知错误原因");
        }

       recode = reMap.get("result_code");
        if(!recode.equals("SUCCESS")){
           return CodeRe.error(reMap.get("err_code")+":"+reMap.get("err_code_des"));
        }

        RedBill redBill = new RedBill();
        if(reMap.containsKey("sign")){
            redBill.setSign(reMap.get("sign"));
        }
        if(reMap.containsKey("mch_billno")){
            redBill.setMchBillno(reMap.get("mch_billno"));
        }
        if(reMap.containsKey("mch_id")){
            redBill.setMchId(reMap.get("mch_id"));
        }
        if(reMap.containsKey("wxappid")){
            redBill.setWxappid(reMap.get("wxappid"));
        }
        if(reMap.containsKey("re_openid")){
            redBill.setReOpenid(reMap.get("re_openid"));
        }
        if(reMap.containsKey("total_amount")){
            redBill.setTotalAmount(Integer.parseInt(reMap.get("total_amount")));
        }
        if(reMap.containsKey("send_listid")){
            redBill.setSendListid(reMap.get("send_listid"));
        }

        persistenceService.updateOrSave(redBill);

        return CodeRe.correct("success");
    }

    @Override
    public CodeRe<RedStatus> payInfo(String billno) {
        RedReqInfo info  = new RedReqInfo();
        info.setNonce_str(UniqueStringGenerator.getUniqueCode());
        info.setMch_id(UniqueStringGenerator.MCH_ID);
        info.setMch_billno(billno);
        info.setAppid("wx653d39223641bea7");
        info.setBill_type("MCHT");
        Map<String,String> map = WxXmlParser.getWxMap(info);
        Set<String> keys = map.keySet();
        Object[] objs =  keys.toArray();
        Arrays.sort(objs);
        StringBuilder builder = new StringBuilder();
        for(int i = 0 ;i <objs.length;i++){
            builder.append(objs[i].toString()).append("=")
                    .append(map.get(objs[i])).append("&");
        }
        builder.append("key=acjkgkliutguizkjgailzsghqyesiu11");
        info.setSign(UniqueStringGenerator.getMd5(builder.toString()));

        HttpResponse response = HttpClientUtils.SSLPostSend("https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo",WxXmlParser.getWxXml(info));
        StringBuilder builder1 = new StringBuilder();
        try(BufferedReader reader =new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"))){
            String line;
            while((line = reader.readLine())!=null){
                builder1.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        Map<String,String> reMap = WxXmlParser.Xml2Map(builder1.toString());

        if(!reMap.containsKey("return_code")){
            return CodeRe.error("failure request");

        }
        String recode = reMap.get("return_code");
        if(!recode.equals("SUCCESS")){
            if(reMap.containsKey("return_msg")){
                return  CodeRe.error(reMap.get("return_msg"));
            }
            return CodeRe.error("未知错误原因");
        }

        recode = reMap.get("result_code");
        if(!recode.equals("SUCCESS")){
            return CodeRe.error(reMap.get("err_code")+":"+reMap.get("err_code_des"));
        }

        RedStatus redStatus = new RedStatus();

        if(reMap.containsKey("mch_billno")){
            redStatus.setMchBillno(reMap.get("mch_billno"));
        }
        if(reMap.containsKey("mch_id")){
            redStatus.setMchId(reMap.get("mch_id"));
        }
        if(reMap.containsKey("status")){
            redStatus.setStatus(reMap.get("status"));
        }
        if(reMap.containsKey("total_amount")){
            redStatus.setTotalAmount(Integer.parseInt(reMap.get("total_amount")));
        }
        if(reMap.containsKey("send_time")){
            try {
                redStatus.setSendTime(new Timestamp(DateUtils.parse(reMap.get("send_time"),"yyyy-MM-dd HH:mm:ss").getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(reMap.containsKey("refund_time")){
            try {
                redStatus.setRefundTime(new Timestamp(DateUtils.parse(reMap.get("refund_time"),"yyyy-MM-dd HH:mm:ss").getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(reMap.containsKey("rcv_time")){
            try {
                redStatus.setRcvTime(new Timestamp(DateUtils.parse(reMap.get("rcv_time"),"yyyy-MM-dd HH:mm:ss").getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(reMap.containsKey("openid")){
            redStatus.setOpenid(reMap.get("openid"));
        }
        persistenceService.updateOrSave(redStatus);

        return CodeRe.correct(redStatus);
    }
}
