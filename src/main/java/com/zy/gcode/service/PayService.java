package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.BatchRe;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.*;
import com.zy.gcode.service.pay.RedPayInfo;
import com.zy.gcode.service.pay.RedReqInfo;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.apache.http.HttpResponse;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public CodeRe pay(String openid, int count,String token,String geappid) {

        GeToken geToken = persistenceService.get(GeToken.class,geappid);
        AppInterface appInterface = persistenceService.get(AppInterface.class,geappid);
        Timestamp updatetime;

        try {
            updatetime = geToken.getUpdateTime();
        } catch (NullPointerException e) {
            return CodeRe.error("token is of invalid");
        }

        if(!geToken.getGeTokenM().equals(token.trim())){
            return  CodeRe.error("token is invalid");
        }


        if(DateUtils.isOutOfDate(updatetime,7000L)){
            return CodeRe.error("token of time out");
        }


        PayCredential payCredential =  persistenceService.get(PayCredential.class,appInterface.getWxAppid());

        RedPayInfo payInfo = new RedPayInfo();
        payInfo.setNonce_str(UniqueStringGenerator.getUniqueCode());
        payInfo.setMch_billno(UniqueStringGenerator.wxbillno());
        payInfo.setMch_id(payCredential.getMchid());
        payInfo.setWxappid(appInterface.getWxAppid());
        payInfo.setSend_name("追游科技");
        payInfo.setRe_openid(openid);
        payInfo.setTotal_num(1);
        payInfo.setTotal_amount(count*100);
        payInfo.setWishing(payCredential.getWishing());
        payInfo.setClient_ip(payCredential.getClientIp());
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
        builder.append("key=").append(payCredential.getKey());
        payInfo.setSign(UniqueStringGenerator.getMd5(builder.toString()));
        HttpResponse response = HttpClientUtils.paySSLSend(payCredential.getMchid(),payCredential.getCredentialPath(),
                "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack",WxXmlParser.getWxXml(payInfo));
        if(response.getStatusLine().getStatusCode()!=200){
            CodeRe.error("发红包 请求微信服务器失败");
        }
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
    public CodeRe<RedStatus> payInfo(String billno,String token,String geappid) {


        GeToken geToken = persistenceService.get(GeToken.class,geappid);
        AppInterface appInterface = persistenceService.get(AppInterface.class,geappid);
        Timestamp updatetime;

        try {
            updatetime = geToken.getUpdateTime();
        } catch (NullPointerException e) {
            return CodeRe.error("token is of invalid");
        }

        if(!geToken.getGeTokenM().equals(token.trim())){
            return  CodeRe.error("token is invalid");
        }


        if(DateUtils.isOutOfDate(updatetime,7000L)){
            return CodeRe.error("token of time out");
        }

      return redinfo(appInterface.getWxAppid(),billno);
    }

    private CodeRe redinfo(String wxappid,String billno){
        PayCredential payCredential =  persistenceService.get(PayCredential.class,wxappid);
        RedReqInfo info  = new RedReqInfo();
        info.setNonce_str(UniqueStringGenerator.getUniqueCode());
        info.setMch_id(payCredential.getMchid());
        info.setMch_billno(billno);
        info.setAppid(wxappid);
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
        builder.append("key=").append(payCredential.getCredentialPath());
        info.setSign(UniqueStringGenerator.getMd5(builder.toString()));

        HttpResponse response = HttpClientUtils.paySSLSend(payCredential.getMchid(),payCredential.getCredentialPath(),
                "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo",WxXmlParser.getWxXml(info));
        if(response.getStatusLine().getStatusCode()!=200){
            CodeRe.error("请求微信服务器失败！获取红包信息");
        }


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
        redStatus.setWxappid(wxappid);
        persistenceService.updateOrSave(redStatus);

        return CodeRe.correct(redStatus);
    }

    @Override
    public CodeRe circularGetPayInfo() {

        List<RedStatus> pushList = new ArrayList<>(512);
        List<String> errorList = new ArrayList<>();

        Date lastReadTime;
        try {
            lastReadTime = DateUtils.parse(Constants.properties.get("pay.lastReadTime").toString(),"yyyy-MM-dd hh:mm:ss");
        } catch (ParseException e) {
            return  CodeRe.error("pay.lastReadTime 格式必须符合yyyy-MM-dd hh:mm:ss");
        }

        DetachedCriteria billCriteria = DetachedCriteria.forClass(RedBill.class);
        billCriteria.add(Restrictions.ge("insert_time",new Timestamp(lastReadTime.getTime())));
        List<RedBill> redBillList =  persistenceService.getList(billCriteria);
        Timestamp maxInsertTime = (Timestamp)persistenceService.max(RedBill.class,"insert_time");
        Constants.properties.setProperty("pay.lastReadTime",DateUtils.format(maxInsertTime,"yyyy-MM-dd hh:mm:ss"));

        redBillList.forEach(redBill ->{
            CodeRe<RedStatus> redStatusCodeRe =   redinfo(redBill.getWxappid(),redBill.getMchBillno());
            if(redStatusCodeRe.isError()){
                StringBuilder builder = new StringBuilder(redStatusCodeRe.getErrorMessage());
                errorList.add(builder.append(":").append(redBill.getMchBillno()).toString());
            }else {
                RedStatus updateAfterRedStatus = redStatusCodeRe.getMessage();
                pushList.add(updateAfterRedStatus);
        }
        });



        DetachedCriteria criteria = DetachedCriteria.forClass(RedStatus.class);
        Disjunction disjunction =  Restrictions.disjunction();
        disjunction.
                add(Restrictions.not(Restrictions.in("status",new String[]{"RECEIVED","REFUND"})));
        criteria.add(disjunction);
        List<RedStatus> redStatuses =  persistenceService.getList(criteria);
        redStatuses.forEach(redStatus -> {
         CodeRe<RedStatus> redStatusCodeRe =  redinfo(redStatus.getWxappid(),redStatus.getMchBillno());
         if(redStatusCodeRe.isError()){
             StringBuilder builder = new StringBuilder(redStatusCodeRe.getErrorMessage());
             errorList.add(builder.append(":").append(redStatus.getMchBillno()).toString());
         }else {
            RedStatus updateAfterRedStatus = redStatusCodeRe.getMessage();
            if(!updateAfterRedStatus.getStatus().equals(redStatus.getStatus())){
                pushList.add(updateAfterRedStatus);
            }
         }
        });
        BatchRe<RedStatus> batchRe = new BatchRe<>();
        if(!errorList.isEmpty()){
            batchRe.setErrorList(errorList);
        }
        batchRe.setTlist(pushList);


        return batchRe;
    }
}
