package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/1/16.
 */
public class GeToken {

    private long geAppid;
    private String geTokenM;
    private String geCodeM;
    private String openid;
    private String wxToken;
    private Timestamp insertTime;
    private Timestamp updateTime;


    public long getGeAppid() {
        return geAppid;
    }

    public void setGeAppid(long geAppid) {
        this.geAppid = geAppid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getWxToken() {
        return wxToken;
    }

    public void setWxToken(String wxToken) {
        this.wxToken = wxToken;
    }

    public String getGeTokenM() {
        return geTokenM;
    }

    public void setGeTokenM(String geTokenM) {
        this.geTokenM = geTokenM;
    }

    public String getGeCodeM() {
        return geCodeM;
    }

    public void setGeCodeM(String geCodeM) {
        this.geCodeM = geCodeM;
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

}
