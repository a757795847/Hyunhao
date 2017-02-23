package com.zy.gcode.oauth;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by admin5 on 17/2/10.
 */
public abstract class AbstractOAuthRequest<T> {
    protected String url;
    protected LinkedHashMap<String,String> params = new LinkedHashMap();
    protected LinkedHashMap<String,String>  body = new LinkedHashMap<>();

    protected AbstractOAuthRequest(String url){
        this.url=url;
    }
    public void setParam(String name,String value){
        params.put(name,value);
    }

    public void setBody(String name,String value){
        body.put(name,value);
    }

    protected String buildParams(){
        StringBuilder builder = new StringBuilder(url).append("?");
        params.forEach((k,v)-> {
            builder.append(k).append("=").append(v).append("&");
        });
        return builder.substring(0,builder.length()-1);
    }

     public  abstract T start();

}
