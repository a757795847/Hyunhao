package com.zy.gcode.service.pay;

/**
 * Created by admin5 on 17/3/31.
 */
public class OpenCondition {
    public static final String COUNT = "count";
    public static final String DAYCOUNT = "dayCount";


    private int count;
    private int dayCount;

    private boolean isSetCount = true;
    private boolean isSetDaycount = true;

    public int getCount() {
        if(isSetCount)
            throw new IllegalArgumentException();
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.isSetCount = false;
    }

    public int getDayCount() {
        if(isSetDaycount)
            throw new IllegalArgumentException();
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
        this.isSetDaycount = false;
    }
}
