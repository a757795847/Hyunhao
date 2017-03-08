package com.zy.gcode.pojo;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Created by admin5 on 17/2/23.
 */
public class ValidData {
    private Long id;
    private String username;
    private String tappid;
    private Timestamp insertime;
    private Date endData;
    private Date beginTime;
    private String zyappid;
    private String useType;
    private Integer residueCount;
    private Integer totalCount;

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

    public Timestamp getInsertime() {
        return insertime;
    }

    public void setInsertime(Timestamp insertime) {
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

    public String getZyappid() {
        return zyappid;
    }

    public void setZyappid(String zyappid) {
        this.zyappid = zyappid;
    }


    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public Integer getResidueCount() {
        return residueCount;
    }

    public void setResidueCount(Integer residueCount) {
        this.residueCount = residueCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
