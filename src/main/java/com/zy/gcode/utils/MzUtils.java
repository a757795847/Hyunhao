package com.zy.gcode.utils;

import java.util.Arrays;

/**
 * Created by admin5 on 17/2/16.
 */
public class MzUtils {
    public static String merge(Object... strs){
        StringBuilder builder = new StringBuilder();
        Arrays.stream(strs).forEach(builder::append);
        return builder.toString();
    }
    public static String[] trimArray(String[] strs){
        int len = strs.length;
        for(int i =0 ; i <len ;i++){
            strs[i]= strs[i].trim();
        }
        return  strs;
    }
}
