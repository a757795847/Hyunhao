package com.zy.gcode.oauth;

import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.MzUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by admin5 on 17/2/10.
 */
public abstract class AbstractOAuthRequest<T> {
    Logger log = LoggerFactory.getLogger(AbstractOAuthRequest.class);

    protected String url;
    protected LinkedHashMap<String, String> params = new LinkedHashMap();
    protected LinkedHashMap<String, String> body = new LinkedHashMap<>();
    protected String suffix = null;

    protected AbstractOAuthRequest(String url) {
        this.url = url;
    }

    public AbstractOAuthRequest setParam(String name, String value) {
        params.put(name, value);
        return this;
    }

    public AbstractOAuthRequest setBody(String name, String value) {
        body.put(name, value);
        return this;
    }

    public AbstractOAuthRequest setSuffix(String suffix){
        this.suffix = suffix;
        return  this;
    }

    protected String buildParams() {
        StringBuilder builder = new StringBuilder(url).append("?");
        params.forEach((k, v) -> {
            builder.append(k).append("=").append(v).append("&");
        });
        if(suffix==null)
        return builder.substring(0, builder.length() - 1);
        return builder.substring(0, builder.length() - 1)+suffix;
}

    protected <T> T getObj(Class<T> clazz) {
        HttpResponse response = HttpClientUtils.getSend(buildParams());
        if (!HttpClientUtils.checkRespons(response)) {
            return null;
        }
        String str = null;
        try {
           str = MzUtils.inputStreamToString(response.getEntity().getContent());
            return Constants.objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.error("ObjectMapper解析出错:"+ str);
            e.printStackTrace();
            return null;
        }
    }

    public abstract T start();

}
