package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/1/16.
 */
public class WechatPublicServer {
    private String tappid;
    private String wxAppid;
    private String userId;
    private Timestamp insertTime;
    private Timestamp updateTime;
    private String scope;
    private String zyappid;
    private String isAuthentication;

    public String getTappid() {
        return tappid;
    }

    public void setTappid(String tappid) {
        this.tappid = tappid;
    }

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getZyappid() {
        return zyappid;
    }

    public void setZyappid(String zyappid) {
        this.zyappid = zyappid;
    }

    public String getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(String isAuthentication) {
        this.isAuthentication = isAuthentication;
    }
}
