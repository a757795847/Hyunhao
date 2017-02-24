package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.utils.Page;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by admin5 on 17/2/15.
 */
public interface IOrderService {
    List<DataOrder> getOrderByStatus(int status, Page page,String userId);
    CodeRe handleCsv(MultipartFile multipartFile);
    CodeRe saveOrderList(List<DataOrder> orderList,String userId);
    CodeRe passAuditing(String uuid);
    CodeRe redSend(String orderno,long strategyid);
}
