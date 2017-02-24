package com.zy.gcode.dao;

import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.utils.Page;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    public <T> List<T> getList(Class<T> clazz, Page page) {
        Criteria criteria = session().createCriteria(clazz);
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
    public void save(Object obj) {
        session().save(obj);
    }

    @Override
    public <T> T getOneByColumn(Class<T> clazz,String... values) {
        int len = values.length;
        if(len%2!=0){
          throw new IllegalArgumentException();
        }

       Criteria criteria =  session().createCriteria(clazz);
        for(int i = 0 ; i< len ;i+=2){
            criteria.add(Restrictions.eq(values[i],values[i+1]));
        }
        return (T)criteria.uniqueResult();
    }

    @Override
    public int count(Class clazz) {
        Long l = (Long) session().createCriteria(clazz).setProjection(Projections.rowCount()).uniqueResult();
        return l.intValue();
    }

    @Override
    public <T> List<T> getListByIn(Class<T> tClass, String column, Object[] objs) {
      return session().createCriteria(tClass)
        .add(Restrictions.in(column,objs)).list();
    }

    @Override
    public <T> List<T> getListByColumn(Class<T> clazz, String... values) {
        int len = values.length;
        if(len%2!=0){
            throw new IllegalArgumentException();
        }

        Criteria criteria =  session().createCriteria(clazz);
        for(int i = 0 ; i< len ;i+=2){
            criteria.add(Restrictions.eq(values[i],values[i+1]));
        }
        return criteria.list();
    }

    @Override
    public void delete(Class clazz, Serializable id) {
        session().delete(get(clazz,id));
    }

    @Override
    public Transaction beginTransaction() {
        return session().beginTransaction();
    }
}
