package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.*;
import com.zy.gcode.service.intef.IWechatService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.MzUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

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
    @Transactional
    public CodeRe sumbit(final String image1, final String image2, final String image3, final String billno, final String openid, final String appid) {
        String path = new StringBuilder(Constants.RED_PICTURE_PATH).append("/").toString();
        DataOrder dataOrder = persistenceService.getOneByColumn(DataOrder.class, "orderNumber", billno);
        WechatPublicServer wechatPublicServer = persistenceService.getOneByColumn(WechatPublicServer.class, "wxAppid", appid, "zyappid", Constants.ZYAPPID);

        if (wechatPublicServer == null) {
            return CodeRe.error("该微信公众号未录入系统");
        }


        if (dataOrder == null) {
            return CodeRe.error("订单不存在");
        }
        if (dataOrder.getGiftState() != 0) {
            return CodeRe.error("订单未处于未申领状态");
        }
        String dataPath = DateUtils.format(new Date(), "yyyyMM");

        if (image1 != null) {
            dataOrder.setCommentFile1(MzUtils.merge(dataPath, "/", wechatPublicServer.getTappid(), ":", billno, "A"));
        }
        if (image2 != null) {
            dataOrder.setCommentFile2(MzUtils.merge(dataPath, "/", wechatPublicServer.getTappid(), ":", billno, "B"));
        }
        if (image3 != null) {
            dataOrder.setCommentFile3(MzUtils.merge(dataPath, "/", wechatPublicServer.getTappid(), ":", billno, "C"));
        }
        CodeRe<TokenConfig> tokenConfigCodeRe = authenticationService.getWxAccessTokenBySecret(appid);
        if (tokenConfigCodeRe.isError()) {
            System.err.println(tokenConfigCodeRe.getErrorMessage());
            return tokenConfigCodeRe;
        }
        String geturl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + tokenConfigCodeRe.getMessage().getToken() + "&media_id=";
        if (image1 != null) {

            boolean flag = HttpClientUtils.fileGetSend(geturl + image1, path + dataOrder.getCommentFile1());
            MediaMap mediaMap = new MediaMap();
            mediaMap.setMediaId(image1);
            mediaMap.setOwner(billno);
            if (flag) {
                mediaMap.setIsPersistence("1");
                mediaMap.setPath(path + dataOrder.getCommentFile1());
            }
            persistenceService.updateOrSave(mediaMap);

        }
        if (image2 != null) {
            boolean flag = HttpClientUtils.fileGetSend(geturl + image2, path + dataOrder.getCommentFile2());
            MediaMap mediaMap = new MediaMap();
            mediaMap.setMediaId(image2);
            mediaMap.setOwner(billno);
            if (flag) {
                mediaMap.setIsPersistence("1");
                mediaMap.setPath(path + dataOrder.getCommentFile2());
            }
            persistenceService.updateOrSave(mediaMap);
        }
        if (image3 != null) {

            boolean flag = HttpClientUtils.fileGetSend(geturl + image3, path + dataOrder.getCommentFile3());
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
        dataOrder.setGiftState(1);
        dataOrder.setApplyDate(new Timestamp(System.currentTimeMillis()));
        persistenceService.updateOrSave(dataOrder);
        return CodeRe.correct("success");
    }


    @Override
    @Transactional
    public WechatPublicServer getWechatPublic(String id) {
        return persistenceService.get(WechatPublicServer.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public WechatUserInfo getUser(String openid) {
        return persistenceService.get(WechatUserInfo.class, openid);
    }
}
