package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/3/31.
 */
public class UserApp {
    public final static int USE_BY_TIME = 0;
    public final static int USE_BY_COUNT = 1;


    private long id;
    private String userId;
    private String appId;
    private Timestamp insertTime;
    private Timestamp updateTime;
    private String deleteFlag;
    private Timestamp begTime;
    private Timestamp endTime;
    private Integer useType;
    private Integer totalCount;
    private Integer residueCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Timestamp getBegTime() {
        return begTime;
    }

    public void setBegTime(Timestamp begTime) {
        this.begTime = begTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getResidueCount() {
        return residueCount;
    }

    public void setResidueCount(Integer residueCount) {
        this.residueCount = residueCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserApp userApp = (UserApp) o;

        if (id != userApp.id) return false;
        if (userId != null ? !userId.equals(userApp.userId) : userApp.userId != null) return false;
        if (appId != null ? !appId.equals(userApp.appId) : userApp.appId != null) return false;
        if (insertTime != null ? !insertTime.equals(userApp.insertTime) : userApp.insertTime != null) return false;
        if (updateTime != null ? !updateTime.equals(userApp.updateTime) : userApp.updateTime != null) return false;
        if (deleteFlag != null ? !deleteFlag.equals(userApp.deleteFlag) : userApp.deleteFlag != null) return false;
        if (begTime != null ? !begTime.equals(userApp.begTime) : userApp.begTime != null) return false;
        if (endTime != null ? !endTime.equals(userApp.endTime) : userApp.endTime != null) return false;
        if (useType != null ? !useType.equals(userApp.useType) : userApp.useType != null) return false;
        if (totalCount != null ? !totalCount.equals(userApp.totalCount) : userApp.totalCount != null) return false;
        if (residueCount != null ? !residueCount.equals(userApp.residueCount) : userApp.residueCount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (appId != null ? appId.hashCode() : 0);
        result = 31 * result + (insertTime != null ? insertTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (deleteFlag != null ? deleteFlag.hashCode() : 0);
        result = 31 * result + (begTime != null ? begTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (useType != null ? useType.hashCode() : 0);
        result = 31 * result + (totalCount != null ? totalCount.hashCode() : 0);
        result = 31 * result + (residueCount != null ? residueCount.hashCode() : 0);
        return result;
    }
}
