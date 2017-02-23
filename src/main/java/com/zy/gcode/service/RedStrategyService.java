package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataStrategy;
import com.zy.gcode.pojo.WxOperator;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin5 on 17/2/22.
 */
@Component
public class RedStrategyService implements IRedStrategyService {
    @Autowired
    PersistenceService persistenceService;


    @Override
    public CodeRe addStrategy(String name, int money, String remark) {
        WxOperator operator = getOperator();
        DataStrategy existStrategy =  persistenceService.getOneByColumn(DataStrategy.class,"name",name,"createUserId",operator.getUsername());
        if(existStrategy != null) {
            return CodeRe.error("红包名字重复");
        }
        DataStrategy strategy = new DataStrategy();
        strategy.setName(name);
        if(money <100){
            return  CodeRe.error("红包金额必须大于1元");
        }

        strategy.setMoney(money);
        strategy.setRemark(remark);
        strategy.setUserId(operator.getUsername());
        persistenceService.save(strategy);
        return CodeRe.correct("success");
    }

    @Override
    public CodeRe listRedStrategy() {
        WxOperator operator = getOperator();
        List list = persistenceService.getListByColumn(DataStrategy.class,"userId",operator.getUsername());
        return CodeRe.correct(list);
    }

    private WxOperator getOperator(){
        return (WxOperator) SecurityUtils.getSubject().getPrincipal();
    }

    @Override
    public CodeRe deleteRedStrategy(long id) {
        persistenceService.delete(DataStrategy.class,id);
        return CodeRe.correct("success");
    }

    @Override
    public CodeRe updateRedStrategy(long id, String name, int money) {
        DataStrategy strategy = new DataStrategy();
        strategy.setName(name);
        strategy.setId(id);
        strategy.setMoney(money);
        return CodeRe.correct("success");
    }
}
