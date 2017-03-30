package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/2/23.
 */
public class ApplicationInfo {
    public static final int PAY_FREE_TO_USE = 0;
    public static final int PAY_BY_COUNT = 1;
    public static final int PAY_BY_RATE = 2;
    public static final int PAY_BY_DAY = 3;


    private String id;
    private String name;
    private Integer price;
    private String description;
    private String openCondition;
    private String applicationInfo;
    private String escapeClause;
    private String imageUrl;
    private String backgroundColor;
    private String deleteFlag;
    private Timestamp insertTime;
    private Timestamp updateTime;
    private String chargeStandard;
    private int openCdn;
    private String openOption;
    private int payCdn;
    private String payOption;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpenCondition() {
        return openCondition;
    }

    public void setOpenCondition(String openCondition) {
        this.openCondition = openCondition;
    }

    public String getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(String applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    public String getEscapeClause() {
        return escapeClause;
    }

    public void setEscapeClause(String escapeClause) {
        this.escapeClause = escapeClause;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
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

    public String getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(String chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    public int getOpenCdn() {
        return openCdn;
    }

    public void setOpenCdn(int openCdn) {
        this.openCdn = openCdn;
    }

    public String getOpenOption() {
        return openOption;
    }

    public void setOpenOption(String openOption) {
        this.openOption = openOption;
    }

    public int getPayCdn() {
        return payCdn;
    }

    public void setPayCdn(int payCdn) {
        this.payCdn = payCdn;
    }

    public String getPayOption() {
        return payOption;
    }

    public void setPayOption(String payOption) {
        this.payOption = payOption;
    }
}
