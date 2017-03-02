package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/3/1.
 */
public class PaySequence {
    private long id;
    private String disorderNum;
    private Integer planCount;
    private String tappid;
    private String createUserId;
    private Timestamp insetTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisorderNum() {
        return disorderNum;
    }

    public void setDisorderNum(String disorderNum) {
        this.disorderNum = disorderNum;
    }

    public Integer getPlanCount() {
        return planCount;
    }

    public void setPlanCount(Integer planCount) {
        this.planCount = planCount;
    }

    public String getTappid() {
        return tappid;
    }

    public void setTappid(String tappid) {
        this.tappid = tappid;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Timestamp getInsetTime() {
        return insetTime;
    }

    public void setInsetTime(Timestamp insetTime) {
        this.insetTime = insetTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaySequence that = (PaySequence) o;

        if (id != that.id) return false;
        if (disorderNum != null ? !disorderNum.equals(that.disorderNum) : that.disorderNum != null) return false;
        if (planCount != null ? !planCount.equals(that.planCount) : that.planCount != null) return false;
        if (tappid != null ? !tappid.equals(that.tappid) : that.tappid != null) return false;
        if (createUserId != null ? !createUserId.equals(that.createUserId) : that.createUserId != null) return false;
        if (insetTime != null ? !insetTime.equals(that.insetTime) : that.insetTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (disorderNum != null ? disorderNum.hashCode() : 0);
        result = 31 * result + (planCount != null ? planCount.hashCode() : 0);
        result = 31 * result + (tappid != null ? tappid.hashCode() : 0);
        result = 31 * result + (createUserId != null ? createUserId.hashCode() : 0);
        result = 31 * result + (insetTime != null ? insetTime.hashCode() : 0);
        return result;
    }
}
