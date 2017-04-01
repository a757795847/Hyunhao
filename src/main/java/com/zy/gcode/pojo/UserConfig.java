package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/4/1.
 */
public class UserConfig {
    private Long id;
    private String wechatOfficialId;
    private String userId;
    private String appId;
    private Timestamp appOpenTime;
    private Timestamp insertTime;
    private Timestamp updateTime;
    private String deleteFlag;
    private String option;
    private String remark;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWechatOfficialId() {
        return wechatOfficialId;
    }

    public void setWechatOfficialId(String wechatOfficialId) {
        this.wechatOfficialId = wechatOfficialId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Timestamp getAppOpenTime() {
        return appOpenTime;
    }

    public void setAppOpenTime(Timestamp appOpenTime) {
        this.appOpenTime = appOpenTime;
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

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
