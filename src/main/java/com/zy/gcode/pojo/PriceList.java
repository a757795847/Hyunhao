package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/2/23.
 */
public class PriceList {
    private long id;
    private String username;
    private String tappid;
    private Integer money;
    private Timestamp insert;
    private String createUserId;
    private String updateUserId;

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

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Timestamp getInsert() {
        return insert;
    }

    public void setInsert(Timestamp insert) {
        this.insert = insert;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceList priceList = (PriceList) o;

        if (id != priceList.id) return false;
        if (username != null ? !username.equals(priceList.username) : priceList.username != null) return false;
        if (tappid != null ? !tappid.equals(priceList.tappid) : priceList.tappid != null) return false;
        if (money != null ? !money.equals(priceList.money) : priceList.money != null) return false;
        if (insert != null ? !insert.equals(priceList.insert) : priceList.insert != null) return false;
        if (createUserId != null ? !createUserId.equals(priceList.createUserId) : priceList.createUserId != null)
            return false;
        if (updateUserId != null ? !updateUserId.equals(priceList.updateUserId) : priceList.updateUserId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (tappid != null ? tappid.hashCode() : 0);
        result = 31 * result + (money != null ? money.hashCode() : 0);
        result = 31 * result + (insert != null ? insert.hashCode() : 0);
        result = 31 * result + (createUserId != null ? createUserId.hashCode() : 0);
        result = 31 * result + (updateUserId != null ? updateUserId.hashCode() : 0);
        return result;
    }
}
