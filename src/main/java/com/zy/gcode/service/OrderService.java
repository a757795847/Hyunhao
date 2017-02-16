package com.zy.gcode.service;

import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.utils.Page;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin5 on 17/2/15.
 */
@Component
public class OrderService implements IOrderService {
    @Autowired
    PersistenceService persistenceService;


    @Override
    public List<DataOrder> getOrderByStatus(int status, Page page) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DataOrder.class);
        criteria.add(Restrictions.eq("giftState",status));
        return persistenceService.getListAndSetCount(DataOrder.class,criteria,page);
    }
}
