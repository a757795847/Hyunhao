package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/4/6.
 */
public class ChargeRecord {

    public static final int BUY_ = 100;
    public static final int SELL_ = 200;

    private long id;
    private Timestamp insertTime;
    private Timestamp updateTime;
    private Short chargeType;
    private String state;
    private String userId;
    private Integer presentCount;
    private Integer chargeCount;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Short getChargeType() {
        return chargeType;
    }

    public void setChargeType(Short chargeType) {
        this.chargeType = chargeType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(Integer presentCount) {
        this.presentCount = presentCount;
    }

    public Integer getChargeCount() {
        return chargeCount;
    }

    public void setChargeCount(Integer chargeCount) {
        this.chargeCount = chargeCount;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargeRecord that = (ChargeRecord) o;

        if (id != that.id) return false;
        if (insertTime != null ? !insertTime.equals(that.insertTime) : that.insertTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (chargeType != null ? !chargeType.equals(that.chargeType) : that.chargeType != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (presentCount != null ? !presentCount.equals(that.presentCount) : that.presentCount != null) return false;
        if (chargeCount != null ? !chargeCount.equals(that.chargeCount) : that.chargeCount != null) return false;
        if (option1 != null ? !option1.equals(that.option1) : that.option1 != null) return false;
        if (option2 != null ? !option2.equals(that.option2) : that.option2 != null) return false;
        if (option3 != null ? !option3.equals(that.option3) : that.option3 != null) return false;
        if (option4 != null ? !option4.equals(that.option4) : that.option4 != null) return false;
        if (option5 != null ? !option5.equals(that.option5) : that.option5 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (insertTime != null ? insertTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (chargeType != null ? chargeType.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (presentCount != null ? presentCount.hashCode() : 0);
        result = 31 * result + (chargeCount != null ? chargeCount.hashCode() : 0);
        result = 31 * result + (option1 != null ? option1.hashCode() : 0);
        result = 31 * result + (option2 != null ? option2.hashCode() : 0);
        result = 31 * result + (option3 != null ? option3.hashCode() : 0);
        result = 31 * result + (option4 != null ? option4.hashCode() : 0);
        result = 31 * result + (option5 != null ? option5.hashCode() : 0);
        return result;
    }
}
