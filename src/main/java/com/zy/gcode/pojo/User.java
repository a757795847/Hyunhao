package com.zy.gcode.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by admin5 on 17/2/15.
 */
public class User implements Serializable {
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
    @JsonIgnore
    private String state;
    private int cash;
    //赠送的现金
    private int presentCash;
    private String email;
    private String headImage;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getPresentCash() {
        return presentCash;
    }

    public void setPresentCash(int presentCash) {
        this.presentCash = presentCash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }
}
