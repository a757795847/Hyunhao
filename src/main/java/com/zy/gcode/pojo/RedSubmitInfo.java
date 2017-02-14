package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/2/14.
 */
public class RedSubmitInfo {
    private String rsNumber;
    private Timestamp insertTime;
    private Timestamp refuseTime;
    private String refuseMark;
    private String redSubmitInfocol;
    private String pic1Path;
    private String pic2Path;
    private String pic3Path;
    private String isSend;
    private String isRefuse;
    private String openId;
    private Integer redAmount;

    public String getRsNumber() {
        return rsNumber;
    }

    public void setRsNumber(String rsNumber) {
        this.rsNumber = rsNumber;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public Timestamp getRefuseTime() {
        return refuseTime;
    }

    public void setRefuseTime(Timestamp refuseTime) {
        this.refuseTime = refuseTime;
    }

    public String getRefuseMark() {
        return refuseMark;
    }

    public void setRefuseMark(String refuseMark) {
        this.refuseMark = refuseMark;
    }

    public String getRedSubmitInfocol() {
        return redSubmitInfocol;
    }

    public void setRedSubmitInfocol(String redSubmitInfocol) {
        this.redSubmitInfocol = redSubmitInfocol;
    }

    public String getPic1Path() {
        return pic1Path;
    }

    public void setPic1Path(String pic1Path) {
        this.pic1Path = pic1Path;
    }

    public String getPic2Path() {
        return pic2Path;
    }

    public void setPic2Path(String pic2Path) {
        this.pic2Path = pic2Path;
    }

    public String getPic3Path() {
        return pic3Path;
    }

    public void setPic3Path(String pic3Path) {
        this.pic3Path = pic3Path;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }

    public String getIsRefuse() {
        return isRefuse;
    }

    public void setIsRefuse(String isRefuse) {
        this.isRefuse = isRefuse;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getRedAmount() {
        return redAmount;
    }

    public void setRedAmount(Integer redAmount) {
        this.redAmount = redAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedSubmitInfo that = (RedSubmitInfo) o;

        if (rsNumber != null ? !rsNumber.equals(that.rsNumber) : that.rsNumber != null) return false;
        if (insertTime != null ? !insertTime.equals(that.insertTime) : that.insertTime != null) return false;
        if (refuseTime != null ? !refuseTime.equals(that.refuseTime) : that.refuseTime != null) return false;
        if (refuseMark != null ? !refuseMark.equals(that.refuseMark) : that.refuseMark != null) return false;
        if (redSubmitInfocol != null ? !redSubmitInfocol.equals(that.redSubmitInfocol) : that.redSubmitInfocol != null)
            return false;
        if (pic1Path != null ? !pic1Path.equals(that.pic1Path) : that.pic1Path != null) return false;
        if (pic2Path != null ? !pic2Path.equals(that.pic2Path) : that.pic2Path != null) return false;
        if (pic3Path != null ? !pic3Path.equals(that.pic3Path) : that.pic3Path != null) return false;
        if (isSend != null ? !isSend.equals(that.isSend) : that.isSend != null) return false;
        if (isRefuse != null ? !isRefuse.equals(that.isRefuse) : that.isRefuse != null) return false;
        if (openId != null ? !openId.equals(that.openId) : that.openId != null) return false;
        if (redAmount != null ? !redAmount.equals(that.redAmount) : that.redAmount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rsNumber != null ? rsNumber.hashCode() : 0;
        result = 31 * result + (insertTime != null ? insertTime.hashCode() : 0);
        result = 31 * result + (refuseTime != null ? refuseTime.hashCode() : 0);
        result = 31 * result + (refuseMark != null ? refuseMark.hashCode() : 0);
        result = 31 * result + (redSubmitInfocol != null ? redSubmitInfocol.hashCode() : 0);
        result = 31 * result + (pic1Path != null ? pic1Path.hashCode() : 0);
        result = 31 * result + (pic2Path != null ? pic2Path.hashCode() : 0);
        result = 31 * result + (pic3Path != null ? pic3Path.hashCode() : 0);
        result = 31 * result + (isSend != null ? isSend.hashCode() : 0);
        result = 31 * result + (isRefuse != null ? isRefuse.hashCode() : 0);
        result = 31 * result + (openId != null ? openId.hashCode() : 0);
        result = 31 * result + (redAmount != null ? redAmount.hashCode() : 0);
        return result;
    }
}
