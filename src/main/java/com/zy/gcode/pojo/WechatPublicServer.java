package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/1/16.
 */
public class WechatPublicServer {
    private String tappid;
    private String wxAppid;
    private String secret;
    private String customerId;
    private Timestamp insertTime;
    private Timestamp updateTime;
    private String scope;
    private String username;
    private String zyappid;

    public String getTappid() {
        return tappid;
    }

    public void setTappid(String geAppid) {
        this.tappid = geAppid;
    }

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getZyappid() {
        return zyappid;
    }

    public void setZyappid(String zyappid) {
        this.zyappid = zyappid;
    }
}
