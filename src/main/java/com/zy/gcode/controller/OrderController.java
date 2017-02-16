package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.service.IOrderService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created by admin5 on 17/2/15.
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    IOrderService orderService;

    @RequestMapping("list")
    public @ResponseBody
    Map
    list(@RequestBody Map map){
        Page page = new Page();
        page.setCurrentPageIndex((Integer) map.get("currentPageIndex"));
    return ControllerStatus.ok(orderService.getOrderByStatus((Integer) map.get("status"),page),page);
    }

    @RequestMapping("picture/{name}")
    public void picture(@PathVariable String name, HttpServletResponse response) throws IOException{
        File file = new File(Constants.RED_PICTURE_PATH+"/"+name);
        if(!file.exists()){
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            int l;
            response.setContentType("image/png");
            OutputStream outputStream = response.getOutputStream();
            final byte[] tmp = new byte[4096];
            while ((l = fileInputStream.read(tmp)) != -1) {
               outputStream.write(tmp);
            }
            outputStream.flush();
            outputStream.close();
        } finally {
            fileInputStream.close();

        }
    }

    @RequestMapping("asy")
    public void asyTest(HttpServletRequest request,HttpServletResponse response){
     AsyncContext asyncContext =  request.startAsync();
     long start = System.currentTimeMillis();
     System.out.println("start:"+start);


     System.out.println("end:"+(System.currentTimeMillis()-start));

    }
}
