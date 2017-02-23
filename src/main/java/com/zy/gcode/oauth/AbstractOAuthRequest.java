package com.zy.gcode.oauth;

/**
 * Created by admin5 on 17/2/10.
 */
public abstract class AbstractOAuthRequest {
    protected String url;

    protected AbstractOAuthRequest(String url){
        this.url=url;
    }

  public  abstract String build();

    protected  class Entry{
        protected Entry(String name){
            this.name=name;
        }

        protected  String name;
        protected String value;
    }
}
