package com.zy.gcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zy.gcode.controller.delegate.BatchRe;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.oauth.UnifyOrderRequest;
import com.zy.gcode.pojo.PayCredential;
import com.zy.gcode.pojo.RedBill;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.service.intef.IPayService;
import com.zy.gcode.service.pay.RedPayInfo;
import com.zy.gcode.service.pay.RedReqInfo;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.*;
import org.apache.http.HttpResponse;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    Logger log = LoggerFactory.getLogger(PayService.class);

    @Autowired
    PersistenceService persistenceService;


    @Override
    @Transactional
    public CodeRe pay(String id, int count, String tappid) {
        WechatPublicServer wechatPublicServer = persistenceService.get(WechatPublicServer.class, tappid);
        PayCredential payCredential = persistenceService.get(PayCredential.class, wechatPublicServer.getWxAppid());
        if (payCredential == null) {
            return CodeRe.error("该微信公众号无商户!");
        }
        RedPayInfo payInfo = new RedPayInfo();
        payInfo.setNonce_str(UniqueStringGenerator.getUniqueCode());
        payInfo.setMch_billno(UniqueStringGenerator.wxbillno(payCredential.getMchid()));
        payInfo.setMch_id(payCredential.getMchid());
        payInfo.setWxappid(wechatPublicServer.getWxAppid());
        payInfo.setSend_name("追游科技");
        payInfo.setRe_openid(id);
        payInfo.setTotal_num(1);
        payInfo.setTotal_amount(count);
        payInfo.setWishing(payCredential.getWishing());
        payInfo.setClient_ip(payCredential.getClientIp());
        payInfo.setAct_name("好评返现");
        payInfo.setRemark("多来多得");
        Map<String, String> map = WxXmlParser.getWxMap(payInfo);
        Set<String> keys = map.keySet();
        Object[] objs = keys.toArray();
        Arrays.sort(objs);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < objs.length; i++) {
            builder.append(objs[i].toString()).append("=")
                    .append(map.get(objs[i])).append("&");
        }
        builder.append("key=").append(payCredential.getKey());
        payInfo.setSign(UniqueStringGenerator.getMd5(builder.toString()));
        HttpResponse response = HttpClientUtils.paySSLSend(payCredential.getMchid(), payCredential.getCredentialPath(),
                "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack", WxXmlParser.getWxXml(payInfo));
        if (response.getStatusLine().getStatusCode() != 200) {
            CodeRe.error("发红包 请求微信服务器失败");
        }
        StringBuilder builder1 = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder1.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> reMap = WxXmlParser.Xml2Map(builder1.toString());

        if (!reMap.containsKey("return_code")) {
            return CodeRe.error("failure request");

        }
        String recode = reMap.get("return_code");
        if (!recode.equals("SUCCESS")) {
            if (reMap.containsKey("return_msg")) {
                return CodeRe.error(reMap.get("return_msg"));
            }
            return CodeRe.error("未知错误原因");
        }

        recode = reMap.get("result_code");
        if (!recode.equals("SUCCESS")) {
            return CodeRe.error(reMap.get("err_code") + ":" + reMap.get("err_code_des"));
        }

        RedBill redBill = new RedBill();
        if (reMap.containsKey("sign")) {
            redBill.setSign(reMap.get("sign"));
        }
        if (reMap.containsKey("mch_billno")) {
            redBill.setMchBillno(reMap.get("mch_billno"));
        }
        if (reMap.containsKey("mch_id")) {
            redBill.setMchId(reMap.get("mch_id"));
        }
        if (reMap.containsKey("wxappid")) {
            redBill.setWxappid(reMap.get("wxappid"));
        }
        if (reMap.containsKey("re_openid")) {
            redBill.setReOpenid(reMap.get("re_openid"));
        }
        if (reMap.containsKey("total_amount")) {
            redBill.setTotalAmount(Integer.parseInt(reMap.get("total_amount")));
        }
        if (reMap.containsKey("send_listid")) {
            redBill.setSendListid(reMap.get("send_listid"));
        }
        redBill.setStatus(0);
        redBill.setTappid(tappid);
        persistenceService.updateOrSave(redBill);

        return CodeRe.correct(redBill.getMchBillno());
    }

    @Override
    @Transactional
    public CodeRe<RedStatus> payInfo(String billno, String tappid) {


        WechatPublicServer wechatPublicServer = persistenceService.get(WechatPublicServer.class, tappid);

        return redinfo(wechatPublicServer.getWxAppid(), billno);
    }

    private CodeRe redinfo(String wxappid, String billno) {
        PayCredential payCredential = persistenceService.get(PayCredential.class, wxappid);
        RedReqInfo info = new RedReqInfo();
        info.setNonce_str(UniqueStringGenerator.getUniqueCode());
        info.setMch_id(payCredential.getMchid());
        info.setMch_billno(billno);
        info.setAppid(wxappid);
        info.setBill_type("MCHT");
        Map<String, String> map = WxXmlParser.getWxMap(info);
        Set<String> keys = map.keySet();
        Object[] objs = keys.toArray();
        Arrays.sort(objs);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < objs.length; i++) {
            builder.append(objs[i].toString()).append("=")
                    .append(map.get(objs[i])).append("&");
        }
        builder.append("key=").append(payCredential.getKey());
        info.setSign(UniqueStringGenerator.getMd5(builder.toString()));

        HttpResponse response = HttpClientUtils.paySSLSend(payCredential.getMchid(), payCredential.getCredentialPath(),
                "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo", WxXmlParser.getWxXml(info));
        if (response.getStatusLine().getStatusCode() != 200) {
            CodeRe.error("请求微信服务器失败！获取红包信息");
        }


        Map<String, String> reMap = null;
        try {
            reMap = WxXmlParser.Xml2Map(MzUtils.inputStreamToString(response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
            return CodeRe.error("系统错误");
        }

        if (!reMap.containsKey("return_code")) {
            return CodeRe.error("failure request");

        }
        String recode = reMap.get("return_code");
        if (!recode.equals("SUCCESS")) {
            if (reMap.containsKey("return_msg")) {
                return CodeRe.error(reMap.get("return_msg"));
            }
            return CodeRe.error("未知错误原因");
        }

        recode = reMap.get("result_code");
        if (!recode.equals("SUCCESS")) {
            return CodeRe.error(reMap.get("err_code") + ":" + reMap.get("err_code_des"));
        }

        RedStatus redStatus = new RedStatus();

        if (reMap.containsKey("mch_billno")) {
            redStatus.setMchBillno(reMap.get("mch_billno"));
        }
        if (reMap.containsKey("mch_id")) {
            redStatus.setMchId(reMap.get("mch_id"));
        }
        if (reMap.containsKey("status")) {
            redStatus.setStatus(reMap.get("status"));
        }
        if (reMap.containsKey("total_amount")) {
            redStatus.setTotalAmount(Integer.parseInt(reMap.get("total_amount")));
        }
        if (reMap.containsKey("send_time")) {
            try {
                redStatus.setSendTime(new Timestamp(DateUtils.parse(reMap.get("send_time"), "yyyy-MM-dd HH:mm:ss").getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (reMap.containsKey("refund_time")) {
            try {
                redStatus.setRefundTime(new Timestamp(DateUtils.parse(reMap.get("refund_time"), "yyyy-MM-dd HH:mm:ss").getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (reMap.containsKey("rcv_time")) {
            try {
                redStatus.setRcvTime(new Timestamp(DateUtils.parse(reMap.get("rcv_time"), "yyyy-MM-dd HH:mm:ss").getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (reMap.containsKey("openid")) {
            redStatus.setOpenid(reMap.get("openid"));
        }
        redStatus.setWxappid(wxappid);
        return CodeRe.correct(redStatus);
    }

    @Override
    @Transactional
    public BatchRe circularGetPayInfo() {

        List<RedStatus> pushList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();

        List<RedBill> redBills = persistenceService.getListByColumn(RedBill.class, "status", 0);

        if (Constants.debug) {
            System.out.println("redBillsSize:" + redBills.size());
        }

        redBills.forEach(redBill -> {
            CodeRe<RedStatus> redStatusCodeRe = redinfo(redBill.getWxappid(), redBill.getMchBillno());
            if (redStatusCodeRe.isError()) {
                StringBuilder builder = new StringBuilder(redStatusCodeRe.getErrorMessage());
                errorList.add(builder.append(":").append(redBill.getMchBillno()).toString());
                log.error("抓取红包失败:" + builder.toString());
            } else {
                RedStatus updateAfterRedStatus = redStatusCodeRe.getMessage();
                pushList.add(updateAfterRedStatus);
                redBill.setStatus(1);
                updateAfterRedStatus.setTappid(redBill.getTappid());
                persistenceService.updateOrSave(updateAfterRedStatus);
                persistenceService.update(redBill);
            }
        });
        BatchRe<RedStatus> batchRe = new BatchRe<>();
        if (!errorList.isEmpty()) {
            batchRe.setErrorList(errorList);
        }
        batchRe.setTlist(pushList);
        return batchRe;
    }

    @Transactional
    public BatchRe pullIllegalBill() {
        List<RedStatus> pushList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        DetachedCriteria criteria = DetachedCriteria.forClass(RedStatus.class);
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.
                add(Restrictions.not(Restrictions.in("status", new String[]{"RECEIVED", "REFUND"})));
        criteria.add(disjunction);
        List<RedStatus> redStatuses = persistenceService.getList(criteria);
        if (Constants.debug) {
            System.out.println("updateRedStatusesSize:" + redStatuses.size());
        }

        redStatuses.forEach(redStatus -> {
            CodeRe<RedStatus> redStatusCodeRe = redinfo(redStatus.getWxappid(), redStatus.getMchBillno());
            if (redStatusCodeRe.isError()) {
                StringBuilder builder = new StringBuilder(redStatusCodeRe.getErrorMessage());
                errorList.add(builder.append(":").append(redStatus.getMchBillno()).toString());
                log.error("抓取红包失败:" + builder.toString());
            } else {
                RedStatus updateAfterRedStatus = redStatusCodeRe.getMessage();
                if (Constants.debug) {
                    System.out.println("updateAfterRedStatus:" + updateAfterRedStatus);
                    System.out.println("updateBeforeRedStatus:" + redStatus);
                }


                if (!updateAfterRedStatus.getStatus().equals(redStatus.getStatus())) {
                    pushList.add(updateAfterRedStatus);
                    if (Constants.debug) {
                        System.out.println("红包更新成功:" + updateAfterRedStatus);
                    }
                    redStatus.setStatus(updateAfterRedStatus.getStatus());
                    persistenceService.update(redStatus);
                }
            }
        });

        BatchRe<RedStatus> batchRe = new BatchRe<>();
        if (!errorList.isEmpty()) {
            batchRe.setErrorList(errorList);
        }
        batchRe.setTlist(pushList);


        return batchRe;
    }

    @Override
    @Transactional
    public void setPayQR(HttpServletResponse response, HttpServletRequest request) {
     //  /wechatPayMessage/handler

        PayCredential payCredential = persistenceService.get(PayCredential.class,"wx653d39223641bea7");
        UnifyOrderRequest unifyOrderRequest = new UnifyOrderRequest();
        unifyOrderRequest.init("wx653d39223641bea7",payCredential.getMchid(),"支付测试",10,request.getRemoteAddr(),
                "http://open.izhuiyou.com/wechatPayMessage/handler","NATIVE",payCredential.getCredentialPath(),payCredential.getKey());
        unifyOrderRequest.setProductId("12235413214070356458");
        Map map = unifyOrderRequest.start();
        Du.pl("map:"+map);
        response.setContentType("image/png");
        response.setHeader("Cache-Control","no-cache");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode((String)map.get("code_url"), BarcodeFormat.QR_CODE,200,200);
            MatrixToImageWriter.writeToStream(bitMatrix,"png",response.getOutputStream());
        } catch (WriterException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
