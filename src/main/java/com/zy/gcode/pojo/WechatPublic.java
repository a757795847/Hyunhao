package com.zy.gcode.pojo;

/**
 * Created by admin5 on 17/2/27.
 */
public class WechatPublic {
    private String wxAppid;
    private String secret;
    private String name;
    private String headImage;

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WechatPublic that = (WechatPublic) o;

        if (wxAppid != null ? !wxAppid.equals(that.wxAppid) : that.wxAppid != null) return false;
        if (secret != null ? !secret.equals(that.secret) : that.secret != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (headImage != null ? !headImage.equals(that.headImage) : that.headImage != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = wxAppid != null ? wxAppid.hashCode() : 0;
        result = 31 * result + (secret != null ? secret.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (headImage != null ? headImage.hashCode() : 0);
        return result;
    }
}
