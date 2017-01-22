package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/1/16.
 */
public class GeToken {
    private String geTokenM;
    private String geCodeM;
    private String openid;
    private String wxToken;
    private Timestamp insertTime;
    private Timestamp updateTime;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeToken geToken = (GeToken) o;

        if (geTokenM != null ? !geTokenM.equals(geToken.geTokenM) : geToken.geTokenM != null) return false;
        if (geCodeM != null ? !geCodeM.equals(geToken.geCodeM) : geToken.geCodeM != null) return false;
        if (insertTime != null ? !insertTime.equals(geToken.insertTime) : geToken.insertTime != null) return false;
        if (updateTime != null ? !updateTime.equals(geToken.updateTime) : geToken.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = geTokenM != null ? geTokenM.hashCode() : 0;
        result = 31 * result + (geCodeM != null ? geCodeM.hashCode() : 0);
        result = 31 * result + (insertTime != null ? insertTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
