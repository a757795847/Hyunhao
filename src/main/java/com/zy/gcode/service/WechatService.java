package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.TokenConfig;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin5 on 17/2/14.
 */
@Component
public class WechatService implements IWechatService {
    @Autowired
    PersistenceService persistenceService;

    @Autowired
    AuthenticationService authenticationService;


    @Override
    public CodeRe sumbit(final String image1,final String image2,final String image3,final String billno,final String openid,final String appid) {
        String path = new StringBuilder(Constants.RED_PICTURE_PATH).append("/").append(billno).toString();
        DataOrder dataOrder = persistenceService.getOneByColumn(DataOrder.class,"orderNumber",billno);

        if(dataOrder==null){
            CodeRe.error("订单不存在");
        }

        if(image1!=null){
            dataOrder.setCommentFile1(billno+"A");
        }
        if(image2!=null){
            dataOrder.setCommentFile2(billno+"B");
        }
        if(image3 !=null){
            dataOrder.setCommentFile3(billno+"C");
        }
        Thread t = new Thread(()->{
            CodeRe<TokenConfig> tokenConfigCodeRe = authenticationService.getWxAccessToken(appid);
            if(tokenConfigCodeRe.isError()){
                Thread.currentThread().interrupt();
            }

            if(image1!=null){
              HttpResponse response = HttpClientUtils.SSLGetSend("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID");
                transferTo(response,path+dataOrder.getCommentFile1());
            }
            if(image2!=null){
                HttpResponse response = HttpClientUtils.SSLGetSend("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID");
                transferTo(response,path+dataOrder.getCommentFile2());
            }
            if(image3 !=null){
                HttpResponse response = HttpClientUtils.SSLGetSend("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID");
                transferTo(response,path+dataOrder.getCommentFile3());
            }
        });

        dataOrder.setWeixinId(openid);
        persistenceService.save(dataOrder);

        return CodeRe.correct("success");
    }

    private boolean transferTo(HttpResponse response,String path){
        File file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            response.getEntity().writeTo(fileOutputStream);

        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
      return true;
    }


}
