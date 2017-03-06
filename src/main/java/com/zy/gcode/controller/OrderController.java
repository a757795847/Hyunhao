package com.zy.gcode.controller;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.controller.delegate.ControllerStatus;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.IOrderService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.Page;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
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
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Timestamp importTime = null;
        Timestamp applyTime = null;
        if (map.containsKey("importTime") && map.get("importTime") != null) {
            importTime = new Timestamp((long) map.get("importTime"));
        }
        if (map.containsKey("applyTime") && map.get("applyTime") != null) {
            applyTime = new Timestamp((long) map.get("applyTime"));
        }

        return ControllerStatus.ok(orderService.getOrderByCondition((Integer) map.get("status"), page, user.getUsername(), applyTime, importTime), page);
    }

    /**
     * 返货订单首页
     *
     * @return
     */
    @RequestMapping("home")
    public String index() {
        return "/views/publicNumber/proxylist.html";
    }

    /**
     * 根据图片的名称访问图片
     *
     * @param name
     * @param response
     * @throws IOException
     */
    @RequestMapping("picture/{date}/{name}")
    public void picture(@PathVariable String date, @PathVariable String name, HttpServletResponse response) throws IOException {
        File file = new File(Constants.RED_PICTURE_PATH + "/" + date + "/" + name);
        if (!file.exists()) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("文件不存在");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        String[] strs = name.split(":");
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("如要访问，请先登录！");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        if (!strs[0].equals(user.getWechatPublicServerList(Constants.ZYAPPID).getTappid())) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("无法访问");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }


        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            file.lastModified();
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");
            response.setHeader("Cache-Control","max-age=3600");
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
    Object parseCsv(MultipartFile file) {
//      User operator = (User) SecurityUtils.getSubject().getSession().getAttribute("operator");
        CodeRe codeRe = orderService.handleCsv(file);
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok((List) codeRe.getMessage());
    }

    /**
     * 根据前段传入的数组导入order数据到DB
     *
     * @param orderList
     * @return
     */
    @RequestMapping("importCsv")
    public
    @ResponseBody
    Object importCsv(@RequestBody List<DataOrder> orderList) {
        User operator = (User) SecurityUtils.getSubject().getPrincipal();
        CodeRe codeRe = orderService.saveOrderList(orderList, operator.getUsername());
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok((List) codeRe.getMessage());
    }

    /**
     * 根据指定的id，设置订单状态为审核通过
     *
     * @param id
     * @return
     */
    @RequestMapping("passAuditing/{id}")
    public
    @ResponseBody
    Map pass(@PathVariable String id) {
        CodeRe codeRe = orderService.passAuditing(id);
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage().toString());
    }

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
        if (SecurityUtils.getSubject().isPermitted(Constants.ZYAPPID)) {
            ControllerStatus.error("无权访问");
        }


        if (!map.containsKey("id")) {
            return ControllerStatus.error("请传入订单id");
        }
        if (!map.containsKey("strategyid")) {
            return ControllerStatus.error("请选择红包策略");
        }
        CodeRe codeRe = orderService.redSend(map.get("id").toString(), Integer.valueOf(map.get("strategyid").toString()).longValue());
        if (codeRe.isError()) {
            return ControllerStatus.error(codeRe.getErrorMessage());
        }
        return ControllerStatus.ok(codeRe.getMessage().toString());

    }

    @RequestMapping("test")
    public
    @ResponseBody
    String test() {
        return SecurityUtils.getSubject().isPermitted(Constants.ZYAPPID) + "";
    }


}
