package com.zy.gcode.service.annocation;

import java.lang.annotation.*;

/**
 * Created by admin5 on 17/2/16.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CsvPush {
    String value();
}
