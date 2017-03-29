package com.zy.gcode.service.intef;

import com.zy.gcode.controller.delegate.CodeRe;

/**
 * Created by admin5 on 17/2/22.
 */
public interface IRedStrategyService {
    CodeRe addStrategy(String name, int money, String remark);

    CodeRe listRedStrategy();

    CodeRe deleteRedStrategy(long id);

    CodeRe updateRedStrategy(long id, String name, int money);
}
