package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataStrategy;
import com.zy.gcode.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by admin5 on 17/2/22.
 */
@Component
public class RedStrategyService implements IRedStrategyService {
    @Autowired
    PersistenceService persistenceService;


    @Override
    @Transactional
    public CodeRe addStrategy(String name, int money, String remark) {
        User operator = getOperator();
        DataStrategy existStrategy =  persistenceService.getOneByColumn(DataStrategy.class,"name",name,"createUserId",operator.getUsername());

        if(persistenceService.count(DataStrategy.class,"createUserId",operator.getUsername())>10){
            return CodeRe.error("红包策略最多只能有10个");
        }

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
        strategy.setCreateUserId(operator.getUsername());
        persistenceService.save(strategy);
        return CodeRe.correct("success");
    }

    @Override
    @Transactional
    public CodeRe listRedStrategy() {
        User operator = getOperator();
        List list = persistenceService.getListByColumn(DataStrategy.class,"userId",operator.getUsername());
        return CodeRe.correct(list);
    }

    private User getOperator(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    @Override
    @Transactional
    public CodeRe deleteRedStrategy(long id) {
        persistenceService.delete(DataStrategy.class,id);
        return CodeRe.correct("success");
    }

    @Override
    @Transactional
    public CodeRe updateRedStrategy(long id, String name, int money) {
        DataStrategy strategy = persistenceService.get(DataStrategy.class,id);
        if(strategy == null){
            return CodeRe.error("更新策略不存在");
        }
        strategy.setName(name);
        strategy.setMoney(money);
        strategy.setUpdateUserId(getOperator().getUsername());

        persistenceService.update(strategy);
        return CodeRe.correct("success");
    }
}
