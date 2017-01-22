package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/1/16.
 */
public class GeCode {
    private String geAppid;
    private Timestamp insertTime;
    private Timestamp updateTime;
    private Long expires;
    private String geCodeM;
    private String callbackUrl;
    private String openid;
    private String wxToken;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getWxToken() {
        return wxToken;
    }

    public void setWxToken(String wxtoken) {
        this.wxToken = wxtoken;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getGeAppid() {
        return geAppid;
    }

    public void setGeAppid(String geAppid) {
        this.geAppid = geAppid;
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

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public String getGeCodeM() {
        return geCodeM;
    }

    public void setGeCodeM(String geCodeM) {
        this.geCodeM = geCodeM;
    }


}
