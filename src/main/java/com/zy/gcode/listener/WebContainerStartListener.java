package com.zy.gcode.listener;

import com.zy.gcode.listener.task.RedPagCatchTask;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.DateUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin5 on 17/2/13.
 */
public class WebContainerStartListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TimerTask timerTask = new RedPagCatchTask(WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()));
        Timer timer = new Timer();
        timer.schedule(timerTask, 100000, 1000 * 60 * 10);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try (OutputStream outputStream = new FileOutputStream(new ClassPathResource("config.properties").getFile())) {
            Constants.properties.store(outputStream, "时间:" + DateUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
