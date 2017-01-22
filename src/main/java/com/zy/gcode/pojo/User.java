package com.zy.gcode.pojo;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/1/16.
 */
public class User {
    private String openId;
    private String phone;
    private String nick;
    private String province;
    private String city;
    private String country;
    private String headImgUrl;
    private String privilege;
    private String unionId;
    private String sex;
    private Timestamp insertTime;
    private Timestamp updateTime;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

        User user = (User) o;

        if (openId != null ? !openId.equals(user.openId) : user.openId != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (nick != null ? !nick.equals(user.nick) : user.nick != null) return false;
        if (province != null ? !province.equals(user.province) : user.province != null) return false;
        if (city != null ? !city.equals(user.city) : user.city != null) return false;
        if (country != null ? !country.equals(user.country) : user.country != null) return false;
        if (headImgUrl != null ? !headImgUrl.equals(user.headImgUrl) : user.headImgUrl != null) return false;
        if (privilege != null ? !privilege.equals(user.privilege) : user.privilege != null) return false;
        if (unionId != null ? !unionId.equals(user.unionId) : user.unionId != null) return false;
        if (sex != null ? !sex.equals(user.sex) : user.sex != null) return false;
        if (insertTime != null ? !insertTime.equals(user.insertTime) : user.insertTime != null) return false;
        if (updateTime != null ? !updateTime.equals(user.updateTime) : user.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = openId != null ? openId.hashCode() : 0;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (nick != null ? nick.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (headImgUrl != null ? headImgUrl.hashCode() : 0);
        result = 31 * result + (privilege != null ? privilege.hashCode() : 0);
        result = 31 * result + (unionId != null ? unionId.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (insertTime != null ? insertTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
