package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/1/22.
 */
public class ComponetToken {
    private String componetAppid;
    private String componentAccessToken;
    private Long expiresIn;
    private Timestamp insertTime;
    private Timestamp updateTime;

    public String getComponetAppid() {
        return componetAppid;
    }

    public void setComponetAppid(String componetAppid) {
        this.componetAppid = componetAppid;
    }

    public String getComponentAccessToken() {
        return componentAccessToken;
    }

    public void setComponentAccessToken(String componentAccessToken) {
        this.componentAccessToken = componentAccessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
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

        ComponetToken that = (ComponetToken) o;

        if (componetAppid != null ? !componetAppid.equals(that.componetAppid) : that.componetAppid != null)
            return false;
        if (componentAccessToken != null ? !componentAccessToken.equals(that.componentAccessToken) : that.componentAccessToken != null)
            return false;
        if (expiresIn != null ? !expiresIn.equals(that.expiresIn) : that.expiresIn != null) return false;
        if (insertTime != null ? !insertTime.equals(that.insertTime) : that.insertTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = componetAppid != null ? componetAppid.hashCode() : 0;
        result = 31 * result + (componentAccessToken != null ? componentAccessToken.hashCode() : 0);
        result = 31 * result + (expiresIn != null ? expiresIn.hashCode() : 0);
        result = 31 * result + (insertTime != null ? insertTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
