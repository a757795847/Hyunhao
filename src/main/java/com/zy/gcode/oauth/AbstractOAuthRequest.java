package com.zy.gcode.oauth;

import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.JsonUtils;
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
    protected String url;
    protected LinkedHashMap<String, String> params = new LinkedHashMap();
    protected LinkedHashMap<String, String> body = new LinkedHashMap<>();
    protected String suffix = null;
    Logger log = LoggerFactory.getLogger(AbstractOAuthRequest.class);

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

    public AbstractOAuthRequest setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    protected String buildParams() {
        StringBuilder builder = new StringBuilder(url).append("?");
        params.forEach((k, v) -> {
            builder.append(k).append("=").append(v).append("&");
        });
        if (suffix == null)
            return builder.substring(0, builder.length() - 1);
        return builder.substring(0, builder.length() - 1) + suffix;
    }

    protected String buildBody() {
        StringBuilder builder = new StringBuilder("{");
        body.forEach((k, v) -> {
            builder.append("\"").append(k).append("\"").append(":")
                    .append("\"").append(v).append("\"").append(",");
        });
        return builder.substring(0, builder.length() - 1) + "}";

    }

    protected <T> T getObj(Class<T> clazz) {
        HttpResponse response = HttpClientUtils.getSend(buildParams());
        HttpClientUtils.checkRespons(response);

        String str = null;
        try {
            str = MzUtils.inputStreamToString(response.getEntity().getContent());
            return JsonUtils.asObj(clazz, str);
        } catch (IOException e) {
            log.error("ObjectMapper解析出错:" + str);
            e.printStackTrace();
            return null;
        }
    }

    public abstract T start();

}
