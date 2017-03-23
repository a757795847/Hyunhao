package com.zy.gcode.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by admin5 on 17/2/15.
 */
public class User {
    private String username;
    @JsonIgnore
    private String password;
    private Timestamp insertTime;
    private String name;
    private String role;
    private Timestamp updateTime;
    private String isEnable;
    private Date endTime;
    private String phone;
    private String isAuthentication;
    private String authenticationTime;
    private List<WechatPublicServer> wechatPublicServerList;

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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(String isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public String getAuthenticationTime() {
        return authenticationTime;
    }

    public void setAuthenticationTime(String authenticationTime) {
        this.authenticationTime = authenticationTime;
    }

    public WechatPublicServer getWechatPublicServerList(String zyappid) {
        for (WechatPublicServer publicServer : wechatPublicServerList) {
            if (publicServer.getZyappid().equals(zyappid)) {
                return publicServer;
            }
        }
        throw new NullPointerException();
    }

    public void setWechatPublicServerList(List<WechatPublicServer> wechatPublicServerList) {
        this.wechatPublicServerList = wechatPublicServerList;
    }
}
