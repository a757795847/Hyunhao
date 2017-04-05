package com.zy.gcode.dao;

import com.zy.gcode.utils.Page;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin5 on 17/1/16.
 */
public interface PersistenceService {
    <T> T get(Class<T> clazz, Serializable id);

    <T> T load(Class<T> clazz, Serializable id);

    <T> List<T> getList(Class<T> clazz);

    <T> List<T> getList(Class<T> clazz, Page page);

    <T> List<T> getList(DetachedCriteria criteria);

    <T> List<T> getListAndSetCount(Class<T> clazz, DetachedCriteria criteria, Page page);

    void remove(Object id);

    void update(Object object);

    void add(Object object);

    void updateOrSave(Object object);

    Integer count(Class clazz);
    Serializable save(Object obj);

    Object max(Class clazz, String column);

    <T> T getOneByColumn(Class<T> clazz, Object... values);

    <T> List<T> getListByColumn(Class<T> clazz, Object... values);

    <T> List<T> getListByColumn(Class<T> clazz, Page page, Object... values);

    <T> List<T> getListByIn(Class<T> tClass, String column, Object... objs);

    void delete(Class clazz, Serializable id);

    Integer count(Class clazz, Object... objs);

    <T> int[] insertBatch(List<T> list,Class<T> clazz,String sql);

}
