package com.zy.gcode.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.JsonUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by admin5 on 17/2/24.
 */
public class PublicPreCodeRequest extends AbstractOAuthRequest<PublicPreCodeRequest.PreAuthCode> {

    public static final String PRA_COMPONENT_ACCESS_TOKEN = "component_access_token";
    public static final String BAY_COMPONENT_APPID = "component_appid";


    public PublicPreCodeRequest() {
        super("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode");
    }

    @Override
    public PreAuthCode start() {
        HttpResponse response = HttpClientUtils.postSend(buildParams(), buildBody());
        HttpClientUtils.checkRespons(response);

        try {
            return JsonUtils.asObj(PreAuthCode.class, response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class PreAuthCode {
        private String preAuthCode;
        private Integer expiresIn;
        private Integer errcode;
        private String errmsg;
        @JsonIgnore
        private boolean error;

        public String getPreAuthCode() {
            return preAuthCode;
        }

        public void setPreAuthCode(String preAuthCode) {
            this.preAuthCode = preAuthCode;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }

        public Integer getErrcode() {
            return errcode;
        }

        public void setErrcode(Integer errcode) {
            this.errcode = errcode;
            setError(true);
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
            setError(true);
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }
    }
}
