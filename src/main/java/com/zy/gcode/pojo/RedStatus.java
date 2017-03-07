package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/1/23.
 */
public class RedStatus {
    private String mchBillno;
    private String mchId;
    private String status;
    private Integer totalAmount;
    private Timestamp sendTime;
    private Timestamp refundTime;
    private Timestamp rcvTime;
    private Timestamp insertTime;
    private Timestamp updateTime;
    private String openid;
    private String wxappid;

    public String getWxappid() {
        return wxappid;
    }

    public void setWxappid(String wxappid) {
        this.wxappid = wxappid;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Timestamp getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Timestamp refundTime) {
        this.refundTime = refundTime;
    }

    public Timestamp getRcvTime() {
        return rcvTime;
    }

    public void setRcvTime(Timestamp rcvTime) {
        this.rcvTime = rcvTime;
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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "RedStatus{" +
                "mchBillno='" + mchBillno + '\'' +
                ", mchId='" + mchId + '\'' +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", sendTime=" + sendTime +
                ", refundTime=" + refundTime +
                ", rcvTime=" + rcvTime +
                ", insertTime=" + insertTime +
                ", updateTime=" + updateTime +
                ", openid='" + openid + '\'' +
                ", wxappid='" + wxappid + '\'' +
                '}';
    }
}
