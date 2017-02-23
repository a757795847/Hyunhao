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

     public  abstract T build();

}
