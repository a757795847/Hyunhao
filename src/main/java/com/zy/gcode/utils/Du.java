package com.zy.gcode.utils;

/**
 * Created by admin5 on 17/3/15.
 */
public abstract class Du {
    public static void pl(Object obj) {
        System.out.println(obj);
    }

    public static void dPl(Object obj) {
        if (Constants.debug) {
            System.out.println(obj);
        }
    }
}
