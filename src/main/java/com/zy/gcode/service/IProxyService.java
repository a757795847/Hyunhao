package com.zy.gcode.service;

import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.utils.Page;

/**
 * Created by admin5 on 17/2/27.
 */
public interface IProxyService {
    CodeRe getZyAppInfo(Page page);
}
