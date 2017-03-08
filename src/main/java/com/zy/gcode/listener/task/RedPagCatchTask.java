package com.zy.gcode.listener.task;

import com.zy.gcode.controller.delegate.BatchRe;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.JsonUtils;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.*;

/**
 * Created by admin5 on 17/2/13.
 */
public class RedPagCatchTask extends TimerTask {
    ApplicationContext applicationContext;

    public RedPagCatchTask(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    IPayService payService = applicationContext.getBean(IPayService.class);

    @Override
    public void run() {
        BatchRe<RedStatus> batchRe = (BatchRe) payService.circularGetPayInfo();
        Map map = new TreeMap();
        map.put("list", batchRe.getTlist());
        map.put("error", batchRe.getErrorList());
        HttpClientUtils.postSend(Constants.properties.getProperty("callback.redinfo"), JsonUtils.objAsString(map));

    }

    public static class TaskTest extends TimerTask {
        int i = 0;

        @Override
        public void run() {
            System.out.println(i++);
        }
    }

    public static void main(String[] args) throws Exception{
        File file = new File("./");
        System.out.println(file.exists());
        Arrays.stream(file.list()).forEach(System.out::println);
    }
}
