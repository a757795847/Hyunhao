package com.zy.gcode.utils;

/**
 * Created by admin5 on 17/2/16.
 */
public class Timing {
    private long startTime;

    private long endTime;

    private long spendTime;

    public void start() {
        startTime = System.currentTimeMillis();
        System.out.println("start:" + startTime);
    }


    public void end() {
        endTime = System.currentTimeMillis();
        spendTime = endTime - startTime;
        System.out.println("end:" + endTime);
        System.out.println("spend:" + spendTime);
    }

    public void middle(String name) {
        System.out.println("currentTime:" + System.currentTimeMillis());
        System.out.println(name + ":" + (System.currentTimeMillis() - startTime));
    }

}
