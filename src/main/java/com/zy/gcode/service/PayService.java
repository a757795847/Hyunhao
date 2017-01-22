package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.service.pay.RedPayInfo;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.UniqueStringGenerator;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin5 on 17/1/20.
 */
@Service
public class PayService implements IPayService {
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
        payInfo.setTotal_amount(count);
        payInfo.setWishing("恭喜");
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
        String xx ="";
        try(BufferedReader reader =new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"))){
            String line;
            while((line = reader.readLine())!=null){
                xx+=line;
            }
        }catch (IOException e){
            e.printStackTrace();
        }



        CodeRe codeRe = new CodeRe();
        codeRe.setMessage(xx);
        return codeRe;
    }

    @Override
    public CodeRe payInfo(String billno) {
        return null;
    }
}
