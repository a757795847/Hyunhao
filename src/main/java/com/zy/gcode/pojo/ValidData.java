package com.zy.gcode.pojo;

import java.sql.Date;

/**
 * Created by admin5 on 17/2/23.
 */
public class ValidData {
    private long id;
    private String username;
    private String tappid;
    private String insertime;
    private Date endData;
    private Date beginTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTappid() {
        return tappid;
    }

    public void setTappid(String tappid) {
        this.tappid = tappid;
    }

    public String getInsertime() {
        return insertime;
    }

    public void setInsertime(String insertime) {
        this.insertime = insertime;
    }

    public Date getEndData() {
        return endData;
    }

    public void setEndData(Date endData) {
        this.endData = endData;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValidData validData = (ValidData) o;

        if (id != validData.id) return false;
        if (username != null ? !username.equals(validData.username) : validData.username != null) return false;
        if (tappid != null ? !tappid.equals(validData.tappid) : validData.tappid != null) return false;
        if (insertime != null ? !insertime.equals(validData.insertime) : validData.insertime != null) return false;
        if (endData != null ? !endData.equals(validData.endData) : validData.endData != null) return false;
        if (beginTime != null ? !beginTime.equals(validData.beginTime) : validData.beginTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (tappid != null ? tappid.hashCode() : 0);
        result = 31 * result + (insertime != null ? insertime.hashCode() : 0);
        result = 31 * result + (endData != null ? endData.hashCode() : 0);
        result = 31 * result + (beginTime != null ? beginTime.hashCode() : 0);
        return result;
    }
}
