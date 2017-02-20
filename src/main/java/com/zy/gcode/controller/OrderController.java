package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.WxOperator;
import com.zy.gcode.service.IOrderService;
import com.zy.gcode.service.annocation.CsvPush;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.Page;
import com.zy.gcode.utils.Timing;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
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
    public String index(){
        return "/views/publicNumber/proxylist.html";
    }

    @RequestMapping("picture/{name}")
    public void picture(@PathVariable String name, HttpServletResponse response) throws IOException{
        File file = new File(Constants.RED_PICTURE_PATH+"/"+name);
        if(!file.exists()){
            response.getWriter().println("文件不存在");
            response.getWriter().flush();
            return;
        }
        String[] strs =   name.split(":");
        WxOperator wxOperator = (WxOperator)SecurityUtils.getSubject().getSession().getAttribute("operator");

        if(!strs[0].equals(wxOperator.getWxAppid())){
            response.getWriter().println("无法访问");
            response.getWriter().flush();
            return;
        }


        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");
            final byte[] tmp = new byte[4096];
            while (( fileInputStream.read(tmp)) != -1) {
               outputStream.write(tmp);
            }
            outputStream.flush();
            outputStream.close();
        } finally {
            fileInputStream.close();

        }
    }
    @RequestMapping("import")
    public String importCsv(){
        return "/views/publicNumber/import.html";
    }

    @RequestMapping(value = "parseCsv",method = RequestMethod.POST)
    public @ResponseBody  Object parseCsv(MultipartFile file){
//      WxOperator operator = (WxOperator) SecurityUtils.getSubject().getSession().getAttribute("operator");
       CodeRe codeRe =  orderService.handleCsv(file,"zhuiyou123");
        if(codeRe.isError()){
            return  ControllerStatus.error(codeRe.getErrorMessage());
        }
      return ControllerStatus.ok((List)codeRe.getMessage());
    }
    @RequestMapping("importCsv")
    public @ResponseBody  String importCsv(@RequestBody List<DataOrder> orderList){
      CodeRe codeRe =  orderService.saveOrderList(orderList);
      return codeRe.getMessage().toString();
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

    @RequestMapping("passAuditing/{id}")
    public @ResponseBody Map pass(@PathVariable String id){
       CodeRe codeRe =  orderService.passAuditing(id);
       if(codeRe.isError()){
           return  ControllerStatus.error(codeRe.getErrorMessage());
       }
       return ControllerStatus.ok(codeRe.getMessage().toString());
    }

    @RequestMapping(value = "redSend",method = RequestMethod.POST )
    public @ResponseBody Object redSend(@RequestBody Map map){
        if(!map.containsKey("id")){
            ControllerStatus.error("请传入订单id");
        }
        if(!map.containsKey("count")){
            ControllerStatus.error("请传入红包大小");
        }
      CodeRe codeRe =  orderService.redSend(map.get("id").toString(),Integer.valueOf(map.get("count").toString()));
        if(codeRe.isError()){
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage().toString());

    }


}
