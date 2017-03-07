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
    private Integer status;
    private String tappid;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTappid() {
        return tappid;
    }

    public void setTappid(String tappid) {
        this.tappid = tappid;
    }
}
