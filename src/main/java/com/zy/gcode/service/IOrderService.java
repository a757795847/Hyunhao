package com.zy.gcode.service;

import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.utils.Page;

import java.util.List;

/**
 * Created by admin5 on 17/2/15.
 */
public interface IOrderService {
    List<DataOrder> getOrderByStatus(int status, Page page);
}
