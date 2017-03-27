package com.zy.gcode.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.gcode.utils.wx.JsapiUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * Created by admin5 on 17/3/23.
 */
public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper>,ApplicationContextAware {
    @Override
    public ObjectMapper getObject() throws Exception {
        Field field = JsonUtils.class.getDeclaredField("mapper");
        field.setAccessible(true);
        return (ObjectMapper)field.get(null);
    }

    @Override
    public Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JwtUtils.setApplicationContext(applicationContext);
    }
}
