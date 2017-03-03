package com.zy.gcode.listener.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zy.gcode.controller.delegate.BatchRe;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;

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
        try {
            HttpClientUtils.postSend(Constants.properties.getProperty("callback.redinfo"), Constants.objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
