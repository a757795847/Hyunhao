package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.service.IOrderService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.Page;
import com.zy.gcode.utils.Timing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
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
    @RequestMapping("home")
    public String index(HttpSession session){
        return "/views/publicNumber/proxyList.html";
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

    @RequestMapping(value = "parseCsv",method = RequestMethod.POST)
    public @ResponseBody  Object parseCsv(MultipartFile file){
       CodeRe codeRe =  orderService.handleCsv(file,"zhang123");
        if(codeRe.isError()){
            return  ControllerStatus.error(codeRe.getErrorMessage());
        }
      return ControllerStatus.ok((List)codeRe.getMessage());
    }



    @RequestMapping("asy")
    public void asyTest(HttpServletRequest request,HttpServletResponse response) throws IOException{
        response.getWriter().println("before");
     AsyncContext asyncContext =  request.startAsync();
     response.getWriter().println("after");
     response.getWriter().flush();
     Timing timing = new Timing();
     timing.start();
     Thread t = new Thread(()->{
        HttpServletResponse httpServletResponse = (HttpServletResponse)asyncContext.getResponse();
         try {
             Thread.currentThread().sleep(10000);
             timing.middle("aysn");
             httpServletResponse.getWriter().println("hello");
             httpServletResponse.getWriter().flush();
             asyncContext.complete();
         } catch (Exception e) {
             e.printStackTrace();
         }
     });
     asyncContext.start(()->System.out.println("runnable"));
     asyncContext.complete();
     t.start();

        timing.end();
    }
}
