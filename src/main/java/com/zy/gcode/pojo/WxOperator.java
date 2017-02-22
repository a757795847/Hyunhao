package com.zy.gcode.pojo;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by admin5 on 17/2/15.
 */
public class WxOperator {
    private String username;
    private String password;
    private Timestamp insertTime;
    private String name;
    private String wxAppid;
    private String role;
    private Timestamp updateTime;
    private String isEnable;
    private Date expireTime;
    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTiem) {
        this.expireTime = expireTiem;
    }
}
