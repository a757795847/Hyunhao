package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/4/7.
 */
public class WechatQrPay {
    private Long id;
    private String wechatOfficalId;
    private String bankType;
    private String feeType;
    private String mchId;
    private String nonceStr;
    private String openid;
    private String outTradeNo;
    private String resultCode;
    private String sign;
    private Timestamp timeEnd;
    private String tradeType;
    private String transactionId;
    private Timestamp insertTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWechatOfficalId() {
        return wechatOfficalId;
    }

    public void setWechatOfficalId(String wechatOfficalId) {
        this.wechatOfficalId = wechatOfficalId;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WechatQrPay that = (WechatQrPay) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (wechatOfficalId != null ? !wechatOfficalId.equals(that.wechatOfficalId) : that.wechatOfficalId != null)
            return false;
        if (bankType != null ? !bankType.equals(that.bankType) : that.bankType != null) return false;
        if (feeType != null ? !feeType.equals(that.feeType) : that.feeType != null) return false;
        if (mchId != null ? !mchId.equals(that.mchId) : that.mchId != null) return false;
        if (nonceStr != null ? !nonceStr.equals(that.nonceStr) : that.nonceStr != null) return false;
        if (openid != null ? !openid.equals(that.openid) : that.openid != null) return false;
        if (outTradeNo != null ? !outTradeNo.equals(that.outTradeNo) : that.outTradeNo != null) return false;
        if (resultCode != null ? !resultCode.equals(that.resultCode) : that.resultCode != null) return false;
        if (sign != null ? !sign.equals(that.sign) : that.sign != null) return false;
        if (timeEnd != null ? !timeEnd.equals(that.timeEnd) : that.timeEnd != null) return false;
        if (tradeType != null ? !tradeType.equals(that.tradeType) : that.tradeType != null) return false;
        if (transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null)
            return false;
        if (insertTime != null ? !insertTime.equals(that.insertTime) : that.insertTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (wechatOfficalId != null ? wechatOfficalId.hashCode() : 0);
        result = 31 * result + (bankType != null ? bankType.hashCode() : 0);
        result = 31 * result + (feeType != null ? feeType.hashCode() : 0);
        result = 31 * result + (mchId != null ? mchId.hashCode() : 0);
        result = 31 * result + (nonceStr != null ? nonceStr.hashCode() : 0);
        result = 31 * result + (openid != null ? openid.hashCode() : 0);
        result = 31 * result + (outTradeNo != null ? outTradeNo.hashCode() : 0);
        result = 31 * result + (resultCode != null ? resultCode.hashCode() : 0);
        result = 31 * result + (sign != null ? sign.hashCode() : 0);
        result = 31 * result + (timeEnd != null ? timeEnd.hashCode() : 0);
        result = 31 * result + (tradeType != null ? tradeType.hashCode() : 0);
        result = 31 * result + (transactionId != null ? transactionId.hashCode() : 0);
        result = 31 * result + (insertTime != null ? insertTime.hashCode() : 0);
        return result;
    }
}
