package com.zy.gcode.dao;

import com.zy.gcode.utils.Du;
import com.zy.gcode.utils.Page;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by admin5 on 17/1/16.
 */
@Component
public class PersistenceServiceImpl implements PersistenceService {
    @Autowired
    SessionFactory sessionFactory;

    public <T> T load(Class<T> clazz, Serializable id) {
        return (T) session().load(clazz, id);
    }

    public <T> T get(Class<T> clazz, Serializable id) {
        return (T) session().get(clazz, id);
    }

    public void updateOrSave(Object object) {
        session().saveOrUpdate(object);
    }

    public Session session() {
        return sessionFactory.getCurrentSession();
    }

    public <T> List<T> getList(Class<T> clazz, Page page) {
        Criteria criteria = session().createCriteria(clazz);
        page.setCount(count(clazz));
        return criteria.setFirstResult(page.getStartIndex()).setMaxResults(page.getPageSize()).list();
    }

    public <T> List<T> getList(DetachedCriteria criteria) {
        return criteria.getExecutableCriteria(session()).list();
    }


    public <T> List<T> getListAndSetCount(Class<T> clazz, DetachedCriteria criteria, Page page) {
        if (page != null) {
            Long l = (Long) criteria.getExecutableCriteria(session()).setProjection(Projections.rowCount()).uniqueResult();
            page.setCount(l.intValue());
            criteria.setProjection(null);
            return criteria.getExecutableCriteria(session()).setFirstResult(page.getStartIndex()).setMaxResults(page.getPageSize()).list();
        }
        return criteria.getExecutableCriteria(session()).list();
    }

    public void remove(Object id) {
        session().delete(id);
    }

    @Transactional
    public void update(Object object) {
        session().update(object);

    }

    @Override
    public <T> List<T> getList(Class<T> clazz) {
        return session().createCriteria(clazz).list();
    }

    public void add(Object object) {
        session().save(object);
    }

    @Override
    public Object max(Class clazz, String column) {

        return Optional.of(session().createCriteria(clazz).setProjection(Projections.max(column)).uniqueResult()).get();
    }

    @Override
    public Serializable save(Object obj) {
        return session().save(obj);
    }

    @Override
    public <T> T getOneByColumn(Class<T> clazz, String... values) {
        int len = values.length;
        if (len % 2 != 0) {
            throw new IllegalArgumentException();
        }

        Criteria criteria = session().createCriteria(clazz);
        for (int i = 0; i < len; i += 2) {
            criteria.add(Restrictions.eq(values[i], values[i + 1]));
        }
        return (T) criteria.uniqueResult();
    }


    @Override
    public <T> List<T> getListByIn(Class<T> tClass, String column, Object... objs) {
        return session().createCriteria(tClass)
                .add(Restrictions.in(column, objs)).list();
    }

    @Override
    public <T> List<T> getListByColumn(Class<T> clazz, Object... values) {
        return getListByColumn(clazz, null, values);
    }

    @Override
    public <T> List<T> getListByColumn(Class<T> clazz, Page page, Object... values) {
        int len = values.length;
        if (len % 2 != 0) {
            throw new IllegalArgumentException();
        }

        Criteria criteria = session().createCriteria(clazz);
        for (int i = 0; i < len; i += 2) {
            if (values[i] == null || values[i + 1] == null)
                continue;
            criteria.add(Restrictions.eq((String) values[i], values[i + 1]));
        }
        if (page != null) {
            Long l = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            page.setCount(l.intValue());
            criteria.setProjection(null);
            criteria.setFirstResult(page.getStartIndex()).setMaxResults(page.getPageSize());
        }
        return criteria.list();
    }

    @Override
    public void delete(Class clazz, Serializable id) {
        session().delete(get(clazz, id));
    }

    @Override
    @Transactional
    public Integer count(Class clazz) {
        Long l = (Long) session().createCriteria(clazz).setProjection(Projections.rowCount()).uniqueResult();
        return l.intValue();
    }

    @Override
    public Integer count(Class clazz, Object[] objs) {
        int len = objs.length;
        if (len % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Criteria criteria = session().createCriteria(clazz);
        for (int i = 0; i < objs.length; i += 2) {
            criteria.add(Restrictions.eq((String) objs[i], objs[i + 1]));
        }
        Long l = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return l.intValue();
    }

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public <T> int[] insertBatch(List<T> list,Class<T> clazz,String sql) {
        Field[] fields = clazz.getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
        }
        int len = fields.length;
       return session().doReturningWork(connection -> {
           System.out.println(connection.toString());
          PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for(Object t:list){
                preparedStatement.setObject(1, UUID.randomUUID().toString());
                for(int i = 2 ; i<=len ;i++){
                    Object value;
                    try {
                        value = fields[i-1].get(t);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        Du.dPl("反射错误!");
                        break;
                    }
                    preparedStatement.setObject(i,value);
                }
                preparedStatement.addBatch();
            }
          return preparedStatement.executeBatch();
        });
    }
}
