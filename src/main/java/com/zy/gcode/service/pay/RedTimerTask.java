package com.zy.gcode.service.pay;

import com.zy.gcode.controller.delegate.BatchRe;
import com.zy.gcode.utils.Constants;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class RedTimerTask extends TimerTask {
    private static final Map<String, RedTimerTask> map = new ConcurrentHashMap();
    private static final Timer timer = new Timer();
    boolean isStop = true;
    Supplier<BatchRe> supplier;

    private RedTimerTask(Supplier supplier) {
        this.supplier = supplier;
    }

    public static RedTimerTask getInstance(Supplier<BatchRe> supplier, String name) {
        RedTimerTask redTimerTask = map.get(name);
        if (redTimerTask == null) {
            redTimerTask = new RedTimerTask(supplier);
            map.put(name, redTimerTask);
        }
        return redTimerTask;
    }

    public boolean begin(int seconds) {
        if (!isStop) {
            return false;
        }
        timer.schedule(this, 5000, seconds * 1000);
        isStop = false;
        return true;
    }

    public boolean cancel() {
        boolean flag = super.cancel();
        if (flag)
            isStop = true;
        return flag;
    }

    @Override
    public void run() {
        BatchRe batchRe = supplier.get();
        if (Constants.debug) {
            System.out.println("ok:" + batchRe.getMessage());
            System.out.println("error:" + batchRe.getErrorList());
        }
    }
}