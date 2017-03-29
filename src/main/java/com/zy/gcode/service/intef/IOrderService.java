package com.zy.gcode.service.intef;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.utils.Page;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by admin5 on 17/2/15.
 */
public interface IOrderService {
    List<DataOrder> getOrderByCondition(List<Integer> status, Page page, String userId, Timestamp applyTime, Timestamp importTime);

    CodeRe handleCsv(MultipartFile multipartFile);

    CodeRe saveOrderList(List<DataOrder> orderList, String userId);

    CodeRe passAuditing(String uuid);

    CodeRe redSend(String orderno, long strategyid);

    CodeRe redInfo(String mchNumber);

    CodeRe getOrderByNumber(String orderNo);
}
