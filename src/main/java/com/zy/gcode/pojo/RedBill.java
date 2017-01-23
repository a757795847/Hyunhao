package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/1/23.
 */
public class RedBill {
    private String mchBillno;
    private String mchId;
    private String wxappid;
    private String reOpenid;
    private Integer totalAmount;
    private String sendListid;
    private String sign;
    private Timestamp insertTime;
    private Timestamp updateTime;

    public String getMchBillno() {
        return mchBillno;
    }

    public void setMchBillno(String mchBillno) {
        this.mchBillno = mchBillno;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getWxappid() {
        return wxappid;
    }

    public void setWxappid(String wxappid) {
        this.wxappid = wxappid;
    }

    public String getReOpenid() {
        return reOpenid;
    }

    public void setReOpenid(String reOpenid) {
        this.reOpenid = reOpenid;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSendListid() {
        return sendListid;
    }

    public void setSendListid(String sendListid) {
        this.sendListid = sendListid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

        RedBill redBill = (RedBill) o;

        if (mchBillno != null ? !mchBillno.equals(redBill.mchBillno) : redBill.mchBillno != null) return false;
        if (mchId != null ? !mchId.equals(redBill.mchId) : redBill.mchId != null) return false;
        if (wxappid != null ? !wxappid.equals(redBill.wxappid) : redBill.wxappid != null) return false;
        if (reOpenid != null ? !reOpenid.equals(redBill.reOpenid) : redBill.reOpenid != null) return false;
        if (totalAmount != null ? !totalAmount.equals(redBill.totalAmount) : redBill.totalAmount != null) return false;
        if (sendListid != null ? !sendListid.equals(redBill.sendListid) : redBill.sendListid != null) return false;
        if (sign != null ? !sign.equals(redBill.sign) : redBill.sign != null) return false;
        if (insertTime != null ? !insertTime.equals(redBill.insertTime) : redBill.insertTime != null) return false;
        if (updateTime != null ? !updateTime.equals(redBill.updateTime) : redBill.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mchBillno != null ? mchBillno.hashCode() : 0;
        result = 31 * result + (mchId != null ? mchId.hashCode() : 0);
        result = 31 * result + (wxappid != null ? wxappid.hashCode() : 0);
        result = 31 * result + (reOpenid != null ? reOpenid.hashCode() : 0);
        result = 31 * result + (totalAmount != null ? totalAmount.hashCode() : 0);
        result = 31 * result + (sendListid != null ? sendListid.hashCode() : 0);
        result = 31 * result + (sign != null ? sign.hashCode() : 0);
        result = 31 * result + (insertTime != null ? insertTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
