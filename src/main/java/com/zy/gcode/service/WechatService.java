package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.MediaMap;
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
           return CodeRe.error("订单不存在");
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
            CodeRe<TokenConfig> tokenConfigCodeRe = authenticationService.getWxAccessToken(appid);
            if(tokenConfigCodeRe.isError()){
                System.err.println(tokenConfigCodeRe.getErrorMessage());
                return tokenConfigCodeRe;
            }
            String geturl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+tokenConfigCodeRe.getMessage().getToken()+"&media_id=";
            if(image1!=null){

                boolean flag =  HttpClientUtils.fileGetSend(geturl + image1, path + dataOrder.getCommentFile1());
                MediaMap mediaMap = new MediaMap();
                mediaMap.setMediaId(image3);
                mediaMap.setOwner(billno);
                if (flag) {
                    mediaMap.setIsPersistence("1");
                    mediaMap.setPath(path + dataOrder.getCommentFile1());
                }
                persistenceService.updateOrSave(mediaMap);

            }
            if(image2!=null){

                boolean flag =  HttpClientUtils.fileGetSend(geturl + image2, path + dataOrder.getCommentFile2());
                MediaMap mediaMap = new MediaMap();
                mediaMap.setMediaId(image2);
                mediaMap.setOwner(billno);
                if (flag) {
                    mediaMap.setIsPersistence("1");
                    mediaMap.setPath(path + dataOrder.getCommentFile2());
                }
                persistenceService.updateOrSave(mediaMap);
            }
            if(image3 !=null) {


                boolean flag =  HttpClientUtils.fileGetSend(geturl + image3, path + dataOrder.getCommentFile3());
                MediaMap mediaMap = new MediaMap();
                mediaMap.setMediaId(image3);
                mediaMap.setOwner(billno);
                if (flag) {
                    mediaMap.setIsPersistence("1");
                    mediaMap.setPath(path + dataOrder.getCommentFile3());
                }
                persistenceService.updateOrSave(mediaMap);
            }


        dataOrder.setWeixinId(openid);
        persistenceService.updateOrSave(dataOrder);

        return CodeRe.correct("success");
    }




}
