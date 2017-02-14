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
    public CodeRe sumbit(String image1,String image2,String image3, String billno,String openid) {
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
            CodeRe<TokenConfig> tokenConfigCodeRe = authenticationService.componetToekn();
            if(tokenConfigCodeRe.isError()){

            }

            if(image1!=null){
              HttpResponse response = HttpClientUtils.SSLGetSend("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID");
            }
            if(image2!=null){
                dataOrder.setCommentFile2(billno+"B");
            }
            if(image3 !=null){
                dataOrder.setCommentFile3(billno+"C");
            }
        });

        dataOrder.setWeixinId(openid);
        persistenceService.save(dataOrder);

        return CodeRe.correct("success");
    }



}
