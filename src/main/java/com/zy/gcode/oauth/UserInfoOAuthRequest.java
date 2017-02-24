package com.zy.gcode.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by admin5 on 17/2/23.
 */
public class UserInfoOAuthRequest extends AbstractOAuthRequest<UserInfoOAuthRequest.UserInfo> {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String OPENID = "openid";

    public UserInfoOAuthRequest(){
        super("https://api.weixin.qq.com/sns/userinfo");
    }

    @Override
    public UserInfo start() {
     return  getObj(UserInfo.class);
    }
/*    public static void main(String[] args) throws  Exception{
        UserInfoOAuthRequest info = new UserInfoOAuthRequest();
        info.setParam(ACCESS_TOKEN,"1111");
        System.out.println(Constants.objectMapper.readValue("{    \"openid\":\" OPENID\",    \"nickname\":\" NICKNAME\",    \"sex\":\"1\",\n" +
                "                \"province\":\"PROVINCE\" ,  \"city\":\"CITY\",    \"country\":\"COUNTRY\",\n" +
                "                \"headimgurl\":    \"http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46\",\n" +
                "                \"privilege\":[ \"PRIVILEGE1\",\"PRIVILEGE2\"],\"unionid\": \"o6_bmasdasdsad6_2sgVt7hMZOPfL\" \n" +
                "        }",UserInfo.class));
    }*/

    public static class UserInfo {
   /*     {    "openid":" OPENID",    " nickname": NICKNAME,    "sex":"1",
                "province":"PROVINCE"    "city":"CITY",    "country":"COUNTRY",
                "headimgurl":    "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
                "privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
        }*/
        private String openid;
        private String nickname;
        private String sex;
        private String province;
        private String city;
        private String country;
        private String headimgurl;
        private String[] privilege;
        private String unionid;
      /*  {"errcode":40003,"errmsg":" invalid openid "}*/
        private  Integer errcode;
        private String errmsg;
        @JsonIgnore
        private boolean error;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
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

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }


        public Integer getErrcode() {
            return errcode;
        }

        public void setErrcode(Integer errcode) {
            setError(true);
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            setError(true);
            this.errmsg = errmsg;
        }

        public String[] getPrivilege() {
            return privilege;
        }

        public void setPrivilege(String[] privilege) {
            this.privilege = privilege;
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }
    }
}
