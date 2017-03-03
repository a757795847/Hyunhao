package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.ApplicationInfo;
import com.zy.gcode.pojo.User;
import com.zy.gcode.pojo.ValidData;
import com.zy.gcode.pojo.WechatPublicServer;
import com.zy.gcode.utils.Page;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Date;
import java.util.*;

/**
 * Created by admin5 on 17/2/27.
 */
@Service
public class ProxyService implements IProxyService {

    @Autowired
    PersistenceService persistenceService;


    @Override
    @Transactional
    public CodeRe getZyAppInfo(String serverType, String isAuthentication, String zyappid, Page page) {

        List<WechatPublicServer> wechatPublicServerList = persistenceService.getListByColumn(WechatPublicServer.class, page, "isAuthentication",
                isAuthentication, "zyappid", zyappid);
        ArrayList results = new ArrayList();
        boolean isServered = false;
        boolean isExpires = false;
        if (serverType != null) {
            if (serverType.equals("1")) {
                isServered = true;
            } else if (serverType.equals("2")) {
                isExpires = true;
            }
        }

        for (WechatPublicServer publicServer : wechatPublicServerList) {
            Map map;
            if (publicServer.getEndTime().getTime() < System.currentTimeMillis()) {
                if (isServered) {
                    page.setCount(page.getCount() - 1);
                    continue;
                }
                map = new LinkedHashMap();
                map.put("serverType", "已过期");

            } else {
                if (isExpires) {
                    page.setCount(page.getCount() - 1);
                    continue;
                }
                map = new LinkedHashMap();
                map.put("serverType", "服务中");
            }
            map.put("beginTime", publicServer.getBeginTime());
            map.put("endTime", publicServer.getEndTime());
            map.put("serverId", publicServer.getTappid());
            map.put("phone", publicServer.getUserId());
            map.put("publicName", publicServer.getWxName());
            map.put("isAuthentication", publicServer.getIsAuthentication());
            ApplicationInfo applicationInfo = persistenceService.get(ApplicationInfo.class, publicServer.getZyappid());
            map.put("zyappName", applicationInfo.getName());
            results.add(map);
        }
        return CodeRe.correct(results);
    }

    @Override
    @Transactional
    public CodeRe topUp(String tappid, int count) {
        WechatPublicServer publicServer = persistenceService.get(WechatPublicServer.class, tappid);
        if (publicServer == null) {
            return CodeRe.error("服务不存在!");
        }
        long endTime = publicServer.getEndTime().getTime();
        long currentTime = System.currentTimeMillis();
        if (endTime > currentTime) {
            publicServer.setEndTime(new Date(endTime + 1000 * 3600 * 24 * 30 * count));
            persistenceService.update(publicServer);
            return CodeRe.correct("充值成功");
        }
        ValidData validData = new ValidData();
        validData.setTappid(tappid);
        validData.setBeginTime(new Date(currentTime));
        validData.setEndData(new Date(currentTime + 1000 * 3600 * 24 * 30 * count));
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        validData.setUsername(user.getUsername());
        Serializable id = persistenceService.save(validData);
        publicServer.setEndTime(validData.getEndData());
        publicServer.setBeginTime(validData.getBeginTime());
        publicServer.setValidDateId((Long) id);
        persistenceService.update(publicServer);
        return CodeRe.correct("重新开通成功");
    }

    @Override
    @Transactional
    public CodeRe topDown(String tappid, int count) {
        return null;
    }

    private final char[] numMap = {'7', '1', '2', '0', '8', '4', '3', '6', '5'};

    private String disorderTo9(Integer num) {
        if (num > 531440) {//最大6位9进制
            num -= 531441;
            return disorderTo9(num);
        }
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(numMap[num % 9]);
            num = num / 9;
            if (num < 9) {
                if (num != 0) {
                    builder.append(numMap[num]);
                }
                break;
            }
        } while (true);
        return getPayCode(builder.reverse().toString());
    }

    private String getPayCode(String str) {
        char[] chars = str.toCharArray();
        StringBuilder builder = new StringBuilder();
        int len = chars.length;
        int flag = 7 - len;
        for (int i = 0; i < len; i++) {
            if (flag > 0 && new Random().nextInt(len) == 0) {
                builder.append(9);
                flag--;
            }
            builder.append(chars[i]);
        }
        if (flag > 0 && new Random().nextInt(len) == 0) {
            builder.append(9);
        }
        return builder.toString();
    }

    @Override
    @Transactional
    public CodeRe setAppPrice(String appid, int count) {
        ApplicationInfo applicationInfo = persistenceService.get(ApplicationInfo.class, appid);
        if (applicationInfo == null) {
            return CodeRe.error("不存在的app");
        }
        applicationInfo.setPrice(count);
        persistenceService.update(applicationInfo);
        return CodeRe.error("success");
    }

    /*    public static void main(String[] args) throws Exception{
        int a=0;
        for(int i = 0 ; i < 6 ; i++){
            a+=8*Math.pow(9,i);
        }
        System.out.println(a);

        ProxyService proxyService = new ProxyService();
        for (int i = 4782000 ; i<4782969;i++){
           System.out.println(proxyService.disorderTo9(i));
            Thread.sleep(1000);
        }
    }*/

}
