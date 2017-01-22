package com.zy.gcode.controller.delegate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin5 on 17/1/18.
 */
public class ControllerStatus {
    public static Map ok(){
        Map map = new HashMap(2);
        map.put("status","1");
        return map;
    }

    public static Map ok(String message){
        Map map = new HashMap(2);
        map.put("status","1");
        map.put("message",message);
        return map;
    }

    public static Map ok(Map map){
        if(map==null){
            map = new HashMap(2);
        }

        map.put("status","1");
        return map;
    }

    public static Map error(){
        Map map = new HashMap(2);
        map.put("status","0");
        return map;
    }
    public static Map error(String message){
        Map map = new HashMap(2);
        map.put("status","0");
        map.put("message",message);
        return map;
    }

    public static Map error(Map map){
        if(map==null){
            map = new HashMap(2);
        }

        map.put("status",0);
        return map;
    }
}
