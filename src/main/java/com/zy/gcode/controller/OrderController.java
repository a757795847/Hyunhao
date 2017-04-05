package com.zy.gcode.controller;

import com.csvreader.CsvWriter;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.intef.IOrderService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.Du;
import com.zy.gcode.utils.Page;
import com.zy.gcode.utils.SubjectUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用于处理用户订单相关
 * Created by admin5 on 17/2/15.
 */
@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    IOrderService orderService;
    @Autowired
    @Qualifier("userCache")
    EhCacheCache userCache;
    /**
     * 分页获取用户信息
     *
     * @param map
     * @return
     */
    @RequestMapping("list")
    public
    @ResponseBody
    Map
    list(@RequestBody Map map) {
        Page page = new Page();
        page.setCurrentPageIndex((Integer) map.get("currentPageIndex"));
        int pageSize = (int)map.get("pageSize");
        page.setPageSize(pageSize);
        Timestamp importTime = null;
        Timestamp applyTime = null;
        if (map.containsKey("importTime") && map.get("importTime") != null) {
            importTime = new Timestamp((long) map.get("importTime"));
        }
        if (map.containsKey("applyTime") && map.get("applyTime") != null) {
            applyTime = new Timestamp((long) map.get("applyTime"));
        }
        return ControllerStatus.ok(orderService.searchOrderByCondition((List) map.get("status"), page,getUsername(), applyTime, importTime), page);
    }

    private String getUsername(){
        return SecurityUtils.getSubject().getPrincipal().toString();
    }

    /**
     * 根据图片的名称访问图片
     *
     * @param name
     * @param webRequest
     * @throws IOException
     */
    @RequestMapping("picture/{date}/{name}")
    public void picture(@PathVariable String date, @PathVariable String name, ServletWebRequest webRequest) throws IOException {
        File file = new File(Constants.RED_PICTURE_PATH + "/" + date + "/" + name);
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse();
        if (!file.exists()) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("文件不存在");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        String[] strs = name.split(":");
        if (!strs[0].equals(SubjectUtils.getUserName())) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("无法访问");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        if (webRequest.checkNotModified(file.lastModified()))
            return;

        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");
            final byte[] tmp = new byte[4096];
            while ((fileInputStream.read(tmp)) != -1) {
                outputStream.write(tmp);
            }
            outputStream.flush();
            outputStream.close();
        } finally {
            fileInputStream.close();
        }
    }

    /**
     * 返回导入csv的页面
     *
     * @return
     */
    @RequestMapping("import")
    public String importCsv() {
        return "/views/publicNumber/import.html";
    }

    /**
     * 解析csv但不导入
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "parseCsv", method = RequestMethod.POST)
    public
    @ResponseBody
    Object parseCsv(MultipartFile file,String label,String redPackageSize){
//      User operator = (User) SecurityUtils.getSubject().getSession().getAttribute("operator");
        if(!file.getOriginalFilename().endsWith("csv")){
            return ControllerStatus.error("必须是csv文件");
        }

        CodeRe codeRe = orderService.handleCsv(file,label,redPackageSize!=null&&!redPackageSize.equals("")?Integer.parseInt(redPackageSize):100);
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage());
    }

    @RequestMapping("downloadErrorList")
    public void downloadErrorList(String key,HttpServletResponse response) throws IOException{
        orderService.downloadErrorOrders(response,key);
    }


/*
    *//**
     * 根据指定的id，设置订单状态为审核通过
     *
     * @param id
     * @return
     *//*
    @RequestMapping("passAuditing/{id}")
    public
    @ResponseBody
    Map pass(@PathVariable String id) {
        CodeRe codeRe = orderService.passAuditing(id);
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage().toString());
    }*/

    /**
     * 审核通过后红包发送
     *
     * @param map
     * @return
     */

    @RequestMapping(value = "redSend", method = RequestMethod.POST)
    public
    @ResponseBody
    Object redSend(@RequestBody Map map) {


        if (!map.containsKey("id")) {
            return ControllerStatus.error("请传入订单id");
        }
        CodeRe codeRe = orderService.redSend(map.get("id").toString());
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage().toString());

    }

    @RequestMapping("/redInfo/{billno}")
    public
    @ResponseBody
    Object redInfo(@PathVariable String billno) {
        CodeRe codeRe = orderService.redInfo(billno);
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage());
    }

    @RequestMapping("lookup/{condition}")
    public
    @ResponseBody
    Object lookup(@PathVariable String condition,@RequestBody Map map) {
        Page page =new Page();
        page.setCurrentPageIndex((int)map.getOrDefault(Page.CURRENTPAGEINDEX,0));
        page.setPageSize((int)map.getOrDefault(Page.PAGE_SIZE,0));

        CodeRe<DataOrder> codeRe = orderService.searchOrderByCondition(condition,page,(List) map.get("status"));
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok((List) codeRe.getMessage(),page);
    }


    @RequestMapping("test")
    public
    @ResponseBody
    String test() {
        return SecurityUtils.getSubject().isPermitted(Constants.ZYAPPID) + "";
    }

   /* public static void main(String[] args) throws Exception{
        AlgorithmParameterGenerator algorithmParameterGenerator = AlgorithmParameterGenerator.getInstance("des");
        algorithmParameterGenerator.init(10);
        algorithmParameterGenerator.
        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("des");
        Du.plbs(new BigInteger("3246346326").toByteArray());
        algorithmParameters.init(new BigInteger("1253125235262346342623215").toByteArray());
        Du.plbs(algorithmParameters.getEncoded());

    }*/

}
